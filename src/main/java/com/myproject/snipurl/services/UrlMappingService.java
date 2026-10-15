
package com.myproject.snipurl.services;

import com.myproject.snipurl.dtos.ClickEventDTO;
import com.myproject.snipurl.dtos.UrlMappingDto;
import com.myproject.snipurl.models.ClickEvent;
import com.myproject.snipurl.models.SnipUser;
import com.myproject.snipurl.models.UrlMapping;
import com.myproject.snipurl.repositories.ClickEventRepository;
import com.myproject.snipurl.repositories.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;
    private final ClickEventRepository clickEventRepository;

    private UrlMappingDto convertToDto(UrlMapping urlMapping) {
        UrlMappingDto dto = new UrlMappingDto();
        dto.setId(urlMapping.getId());
        dto.setOriginalUrl(urlMapping.getActualUrl());
        dto.setShortUrl(urlMapping.getSnipUrl());
        dto.setClickCount(urlMapping.getClickCount());
        dto.setCreatedDate(urlMapping.getCreatedAt());
        dto.setUsername(urlMapping.getUser().getUsername());
        return dto;
    }

    private String generateShortUrl() {
        String characters =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            shortUrl.append(
                    characters.charAt(random.nextInt(characters.length()))
            );
        }

        return shortUrl.toString();
    }

    public UrlMappingDto createShortUrl(String originalUrl, SnipUser user) {
        String shortUrl;

        // Ensure the generated short URL is not already in use.
        do {
            shortUrl = generateShortUrl();
        } while (urlMappingRepository.findBySnipUrl(shortUrl) != null);

        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setActualUrl(originalUrl);
        urlMapping.setSnipUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setDeleted(false);

        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return convertToDto(savedUrlMapping);
    }

    public List<UrlMappingDto> getUrlsByUser(SnipUser user) {
        return urlMappingRepository.findByUserAndDeletedFalse(user)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<ClickEventDTO> getClickEventsByDate(
            String shortUrl, LocalDateTime start, LocalDateTime end) {

        UrlMapping urlMapping = urlMappingRepository
                .findBySnipUrlAndDeletedFalse(shortUrl)
                .orElse(null);

        if (urlMapping == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Active URL not found"
            );
        }

        return clickEventRepository
                .findByUrlMappingAndClickDateBetween(urlMapping, start, end)
                .stream()
                .collect(Collectors.groupingBy(
                        click -> click.getClickDate().toLocalDate(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    ClickEventDTO dto = new ClickEventDTO();
                    dto.setClickDate(entry.getKey());
                    dto.setCount(entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(
            SnipUser user, LocalDate start, LocalDate end) {

        List<UrlMapping> urlMappings =
                urlMappingRepository.findByUserAndDeletedFalse(user);

        if (urlMappings.isEmpty()) {
            return Map.of();
        }

        List<ClickEvent> clickEvents =
                clickEventRepository.findByUrlMappingInAndClickDateBetween(
                        urlMappings,
                        start.atStartOfDay(),
                        end.plusDays(1).atStartOfDay()
                );

        return clickEvents.stream()
                .collect(Collectors.groupingBy(
                        click -> click.getClickDate().toLocalDate(),
                        Collectors.counting()
                ));
    }

    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository
                .findBySnipUrlAndDeletedFalse(shortUrl)
                .orElse(null);

        if (urlMapping != null) {
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingRepository.save(urlMapping);

            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(urlMapping);
            clickEventRepository.save(clickEvent);
        }

        return urlMapping;
    }

    @Transactional
    public void softDeleteUrl(String shortUrl, SnipUser user) {
        UrlMapping urlMapping = urlMappingRepository
                .findBySnipUrlAndUserAndDeletedFalse(shortUrl, user)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "URL not found or you do not have permission to delete it"
                ));

        urlMapping.setDeleted(true);
        urlMappingRepository.save(urlMapping);
    }
}
