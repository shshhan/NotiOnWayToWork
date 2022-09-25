package com.shawn.notification.domain;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SeoulMetro {

    private final String SEOUL_METRO_NOTICE = "http://www.seoulmetro.co.kr/kr/board.do?menuIdx=546";
    private final String SEOUL_METRO = "http://www.seoulmetro.co.kr/kr/";
    private final LocalDate now = LocalDate.now();


    private List<Element> crawlTitle() throws IOException {
            return Jsoup.connect(SEOUL_METRO_NOTICE).get()
                    .getElementsByTag("table").get(0)
                    .getElementsByTag("a")
                    .stream()
                    .filter(el -> el.attr("title").contains("운행 지연"))
                    .collect(Collectors.toList());
    }

    private List<Elements> crawlBody() throws IOException {
        this.findTitleForTodayAndTomorrow(this.crawlTitle());
        List<Elements> pList = new ArrayList<>();
        this.crawlTitle().forEach(el -> {
            log.info(el.html());
            try {
                pList.add(
                        Jsoup.connect(SEOUL_METRO+el.attr("href")).get()
                                .getElementsByTag("tbody").get(0)
                                .getElementsByTag("tr").get(2)
                                .getElementsByClass("txc-textbox").get(0)
                                .getElementsByTag("p")
                        );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return pList;
    }

    public void findTitleForTodayAndTomorrow(List<Element> elementList) {
        elementList.forEach(el -> {
            String dateFromTitle = el.html().replaceAll("([^0-9~/])", "");
            if(isSoon(dateFromTitle)){

            }
        });

    }

    private boolean isSoon(String dateFromTitle){
//        this.periodFormatter(dateFromTitle);
//        if (now.isEqual(parsedFirstDate) || now.plusDays(1).isEqual(parsedFirstDate)) {
//
//
//        } else {
//            return false;
//        }
//
        return true;
    }

    public List<LocalDate> periodFormatter(String dateFromTitle) {
        String[] dates = dateFromTitle  // 09/20 or 09/20~25 or 09/20~09/25
                .replaceAll(" ", "")
                .split("~|,");

        LocalDate parsedStartDate = LocalDate.of(
                now.getYear(),
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

    public LocalDate formatStringToLocalDate(String date){
        DateTimeFormatter df;

        if(date.contains("/")) {
            df = DateTimeFormatter.ofPattern(now.getYear() + "/MM/dd");
        } else {
            df = DateTimeFormatter.ofPattern(now.getYear() + "/" + now.getMonthValue() + "/dd");
        }

        return LocalDate.parse(date, df);
    }

    public void crawlInformation() throws IOException {
        this.crawlBody().forEach(elements -> {
            StringBuilder sb = new StringBuilder();
            elements.forEach(el -> {
                sb.append(el.html()
                        .replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "")
                        .replaceAll("&nbsp;", " ")
                );
            });
            log.info(sb.toString());
        });
    }

}
