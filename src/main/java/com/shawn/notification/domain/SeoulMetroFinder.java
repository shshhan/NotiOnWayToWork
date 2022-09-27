package com.shawn.notification.domain;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SeoulMetroFinder {

    private final String SEOUL_METRO_NOTICE = "http://www.seoulmetro.co.kr/kr/board.do?menuIdx=546";
    private final String SEOUL_METRO = "http://www.seoulmetro.co.kr/kr/";
    private final LocalDate today = LocalDate.now();
    private final LocalDateTime now = LocalDateTime.now();


    public List<Element> crawlTitleForSoon() throws IOException {
        return Jsoup.connect(SEOUL_METRO_NOTICE).get()
                .getElementsByTag("table").get(0)
                .getElementsByTag("a")
                .stream()
                .filter(el -> el.attr("title").contains("운행 지연"))
                .filter(el -> this.isSoon(el.text()))
                .collect(Collectors.toList());
    }

    private boolean isSoon(String elHtml) {
        boolean returnVal = false;

        for (LocalDate date : this.periodFormatter(elHtml.replaceAll("([^0-9~/])", ""))) {

            if( (now.getHour() > 12 && today.plusDays(1).isEqual(date))
                || (now.getHour() < 12 && today.isEqual(date)) ) {
                returnVal = true;
            }
        }

        return returnVal;
    }

    public List<LocalDate> periodFormatter(String dateFromTitle) {
        String[] dates = dateFromTitle  // 09/20 or 09/20~25 or 09/20~09/25
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

    public List<Element> crawlBody(Element title) throws IOException {
        return new ArrayList<>(Jsoup.connect(SEOUL_METRO + title.attr("href")).get()
                .getElementsByTag("tbody").get(0)
                .getElementsByTag("tr").get(2)
                .getElementsByClass("txc-textbox").get(0)
                .getElementsByTag("p"));
    }

    public String crawlInformation(List<Element> pList) throws IOException {
        StringBuilder sb = new StringBuilder();
        pList.forEach(el -> {
            sb.append(el.text());
        });
        return sb.toString();
    }

}
