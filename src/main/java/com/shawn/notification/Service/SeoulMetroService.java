package com.shawn.notification.Service;

import com.shawn.notification.domain.SeoulMetroRepository;
import com.shawn.notification.dto.SeoulMetroDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Slf4j
@RequiredArgsConstructor
@Service
public class SeoulMetroService implements Collector {

    private final SeoulMetroRepository seoulMetroRepository;

    @Value("${metro.notice}")
    private String SEOUL_METRO_NOTICE;
    @Value("${metro.domain}")
    private String SEOUL_METRO;

    private List<SeoulMetroDto> seoulMetroDtoList = new ArrayList<>();

    @Override
    public void collectInformation(LocalDate today, LocalDateTime now) {
        List<Element> titleList = new ArrayList<>();
        List<String> bodyList = new ArrayList<>();

        try {
            titleList = this.crawlTitlesByDate(today, now);

            for (Element titleEl : titleList){
                String title = titleEl.text();
                log.info(title);
                seoulMetroRepository.findByTitle(title)
                        .ifPresent((sm) -> {
                            throw new RuntimeException("이미 등록된 게시물입니다.");
                        });

                bodyList.add(this.crawlBody(titleEl));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < titleList.size(); i++) {
            this.seoulMetroDtoList.add(new SeoulMetroDto(titleList.get(i).text(), bodyList.get(i)));
        }

    }

    @Override
    @Transactional
    public void saveInformation() {
        this.seoulMetroDtoList.forEach(dto -> {
            log.info(dto.toString());
            seoulMetroRepository.save(dto.toEntity());
        });
    }


    public List<Element> crawlTitlesByDate(LocalDate today, LocalDateTime now) throws IOException {
        log.debug("SEOUL_METRO_NOTICE : {}", SEOUL_METRO_NOTICE);
        return Jsoup.connect(SEOUL_METRO_NOTICE).get()
                .getElementsByTag("table").get(0)
                .getElementsByTag("a")
                .stream()
                .filter(el -> el.attr("title").contains("운행 지연"))
                .filter(el -> this.isSoon(el.text(), today, now))
                .collect(Collectors.toList());
    }

    private boolean isSoon(String elHtml, LocalDate today, LocalDateTime now) {
        boolean returnVal = false;

        for (LocalDate date : this.periodFormatter(elHtml.replaceAll("([^0-9~/,])", ""), today)) {

            if( (now.getHour() > 12 && today.plusDays(1).isEqual(date))
                || (now.getHour() < 12 && today.isEqual(date)) ) {
                returnVal = true;
            }
        }

        return returnVal;
    }

    public List<LocalDate> periodFormatter(String dateFromTitle, LocalDate today) {
        String[] dates = dateFromTitle  // 09/20 or 09/20~25 or 09/20~09/25 or 9/20, 21 or 9/20, 9/21
                .replaceAll(" ", "")
                .split("~|,");

        LocalDate parsedStartDate = LocalDate.of(
                today.getYear(),
                Integer.parseInt(dates[0].split("/")[0]),
                Integer.parseInt(dates[0].split("/")[1])
        );

        LocalDate parsedEndDate = parsedStartDate;

        if(dates.length > 1){

            if(dates[1].contains("/")) {
                parsedEndDate = LocalDate.of(
                        parsedStartDate.getYear(),
                        Integer.parseInt(dates[1].split("/")[0]),
                        Integer.parseInt(dates[1].split("/")[1])
                );
            } else {
                parsedEndDate = LocalDate.of(
                        parsedStartDate.getYear(),
                        parsedStartDate.getMonth(),
                        Integer.parseInt(dates[1])
                );
            }

        }

        List<LocalDate> period = new ArrayList<>();

        if(dateFromTitle.contains("~")) {
            for (int i = 0; parsedStartDate.plusDays(i).isBefore(parsedEndDate); i++) {
                period.add(parsedStartDate.plusDays(i));
            }
        } else if (dateFromTitle.contains(",")){
            period.add(parsedStartDate);
        }
        period.add(parsedEndDate);

        return period;
    }

    public String crawlBody(Element title) throws IOException {
        StringBuilder sb = new StringBuilder();
        Jsoup.connect(SEOUL_METRO + title.attr("href"))
                .get()
                .getElementsByTag("tbody").get(0)
                .getElementsByTag("tr").get(2)
                .getElementsByClass("txc-textbox").get(0)
                .getElementsByTag("p")
                .stream().forEach(p -> {
                    sb.append(p.text());
                });
        return sb.toString();
    }

}
