package BotArtem;

import org.jsoup.Jsoup;
import java.io.IOException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArtemBot extends TelegramLongPollingBot {
    public void onUpdateReceived(Update update){
        if(update.hasMessage() && update.getMessage().hasText()){
            String input_mess = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String user_name = update.getMessage().getChat().getFirstName();
            String output_mess = null;
            System.out.println(user_name +  ":" + input_mess);

            SendMessage output = new SendMessage();

            ReplyKeyboardMarkup key1  = new ReplyKeyboardMarkup();
            List<KeyboardRow> key2 = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            row.add("Погода");
            row.add("Курс валют");
            row.add("none");
            key2.add(row);
            row = new KeyboardRow();
            row.add("Настройки");
            key2.add(row);
            key1.setKeyboard(key2);
            key1.setResizeKeyboard(true);

            switch(input_mess) {
                case "/start":
                    output_mess = "Приветствую тебя, " + user_name + "." + " Чем могу помочь?";
                    output.setReplyMarkup(key1);
                    break;
                case "Привет Аня":
                    output_mess = "Привет, " + user_name + ".";
                    break;
                case "Погода":
                    ReplyKeyboardMarkup key_pogod1 = new ReplyKeyboardMarkup();
                    List<KeyboardRow> key_pogod2 = new ArrayList<>();
                    KeyboardRow pog_row = new KeyboardRow();
                    pog_row.add("Погода на сегодня");
                    pog_row.add("Погода на завтра");
                    pog_row.add("Погода на неделю");
                    key_pogod2.add(pog_row);
                    pog_row = new KeyboardRow();
                    pog_row.add("Назад");
                    key_pogod2.add(pog_row);
                    key_pogod1.setKeyboard(key_pogod2);
                    key_pogod1.setResizeKeyboard(true);
                    output.setReplyMarkup(key_pogod1);

                    output_mess = "Выберите прогноз";
                    break;

                case "Погода на сегодня":
                    org.jsoup.nodes.Document doc = null;
                    try {
                        doc = Jsoup.connect("https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0").get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String tooday_temp = doc.select("p.today-temp").text();

                    String subsc = doc.select("#bd1c > div.wDescription.clearfix > div.rSide > div").text();

                    output_mess = "Сейчас в Полтаве: " + tooday_temp + "." + "\n" + subsc;
                    break;
                case "Погода на завтра":
                    Calendar toomo_date = Calendar.getInstance();
                    int year = toomo_date.get(Calendar.YEAR);
                    int month = toomo_date.get(Calendar.MONTH);
                    int day = toomo_date.get(Calendar.DAY_OF_MONTH);
                    if ((month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9) && day == 32)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 31
                    }
                    if(month == 11 && day == 31){
                        day = 1;// Переход с декабря на январь
                        month = 0;
                    }
                    if ((month == 3 || month == 5 || month == 8 || month == 10 ) && day == 31)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 30
                    }
                    if((year == 2020 || year == 2024 || year == 2028 || year == 2032) && day == 29 && month == 2)
                    {
                        day = 1;//Высокосный год
                        month++;//Февраль
                    }
                    if((year == 2018 || year == 2019 || year == 2021 || year == 2022 || year == 2023 || year == 2025 || year == 2026 || year == 2027 || year == 2029 || year == 2030 || year == 2031) && day == 28 && month == 2)
                    {
                        day = 1;// Не высокосный год
                        month++;// Февраль
                    }
                    day++;
                    month++;

                    org.jsoup.nodes.Document toomorow = null;
                    String url = null;
                    if(day < 10){
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-0" + day; ;
                    }
                    else {
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-" + day;
                    }
                    //String url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-" + day;
                    try {
                        toomorow = Jsoup.connect(url).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String toom_temp_min = toomorow.select("#bd2 > div.temperature > div.min > span").text();
                    String toom_temp_max = toomorow.select("#bd2 > div.temperature > div.max > span").text();
                    String toom_subsc = toomorow.select("#bd2c > div.wDescription.clearfix > div.rSide > div").text();
                    output_mess = "Завтра в Полтаве: " +"\n min: " + toom_temp_min + " max: " + toom_temp_max + "." + "\n" + toom_subsc;

                    System.out.println("Year: " + year + " Month: " + month + " Day: " + day);
                    break;
                case "Погода на неделю":

                    output_mess =  "Погода на неделю:";

                    toomo_date = Calendar.getInstance();
                    year = toomo_date.get(Calendar.YEAR);
                    month = toomo_date.get(Calendar.MONTH);
                    day = toomo_date.get(Calendar.DAY_OF_MONTH);


                        org.jsoup.nodes.Document week = null;

                        day++;
                        if ((month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9) && day == 32)
                        {
                            day = 1;// Переход на следующий месяц
                            month++;// Кол-во дней = 31
                        }
                        if(month == 11 && day == 31)
                        {
                            day = 1;// Переход с декабря на январь
                            month = 0;
                        }
                        if ((month == 3 || month == 5 || month == 8 || month == 10) && day == 31)
                        {
                            day = 1;// Переход на следующий месяц
                            month++;// Кол-во дней = 30
                        }
                        if((year == 2020 || year == 2024 || year == 2028 || year == 2032) && day == 29 && month == 2)
                        {
                            day = 1;//Высокосный год
                            month++;//Февраль
                        }
                        if((year == 2018 || year == 2019 || year == 2021 || year == 2022 || year == 2023 || year == 2025 || year == 2026 || year == 2027 || year == 2029 || year == 2030 || year == 2031) && day == 28 && month == 2)
                        {
                            day = 1;// Не высокосный год
                            month++;// Февраль
                        }
                        month++;
                        if(day < 10){
                            url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-0" + day; ;
                        }
                        else {
                            url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-" + day;
                        }
                        try {
                            week = Jsoup.connect(url).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String month_str = null;

                        switch (month){
                            case 1:
                                month_str = "Января";
                                break;
                            case 2:
                                month_str = "Февраля";
                                break;
                            case 3:
                                month_str = "Марта";
                                break;
                            case 4:
                                month_str = "Апреля";
                                break;
                            case 5:
                                month_str = "Майя";
                                break;
                            case 6:
                                month_str = "Июня";
                                break;
                            case 7:
                                month_str = "Июля";
                                break;
                            case 8:
                                month_str = "Августа";
                                break;
                            case 9:
                                month_str = "Сентября";
                                break;
                            case 10:
                                month_str = "Октября";
                                break;
                            case 11:
                                month_str = "Ноября";
                                break;
                            case 12:
                                month_str = "Декабря";
                                break;
                        }

                        String week_url_min1 =  "#bd2 > div.temperature > div.min > span";
                        String week_url_max1 = "#bd2 > div.temperature > div.max > span";
                        String week_url_subsc1 = "#bd2c > div.wDescription.clearfix > div.rSide > div";
                        String week_temp_min1 = week.select(week_url_min1).text();
                        String week_temp_max1 = week.select(week_url_max1).text();
                        String week_subsc1 = week.select(week_url_subsc1).text();
                        output_mess = output_mess +"\n" + day +"-" + month_str + " " + "min: " + week_temp_min1 + " max: " + week_temp_max1 + "." + "\n" + week_subsc1;



                    week = null;

                    day++;
                    if ((month == 1 || month == 3|| month == 5 || month == 7 || month == 8 || month == 10) && day == 32)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 31
                    }
                    if(month == 12 && day == 31)
                    {
                        day = 1;// Переход с декабря на январь
                        month = 0;
                    }
                    if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 30
                    }
                    if((year == 2020 || year == 2024 || year == 2028 || year == 2032) && day == 29 && month == 2)
                    {
                        day = 1;//Высокосный год
                        month++;//Февраль
                    }
                    if((year == 2018 || year == 2019 || year == 2021 || year == 2022 || year == 2023 || year == 2025 || year == 2026 || year == 2027 || year == 2029 || year == 2030 || year == 2031) && day == 28 && month == 2)
                    {
                        day = 1;// Не высокосный год
                        month++;// Февраль
                    }
                    if(day < 10){
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-0" + day; ;
                    }
                    else {
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-" + day;
                    }
                    try {
                        week = Jsoup.connect(url).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    switch (month){
                        case 1:
                            month_str = "Января";
                            break;
                        case 2:
                            month_str = "Февраля";
                            break;
                        case 3:
                            month_str = "Марта";
                            break;
                        case 4:
                            month_str = "Апреля";
                            break;
                        case 5:
                            month_str = "Майя";
                            break;
                        case 6:
                            month_str = "Июня";
                            break;
                        case 7:
                            month_str = "Июля";
                            break;
                        case 8:
                            month_str = "Августа";
                            break;
                        case 9:
                            month_str = "Сентября";
                            break;
                        case 10:
                            month_str = "Октября";
                            break;
                        case 11:
                            month_str = "Ноября";
                            break;
                        case 12:
                            month_str = "Декабря";
                            break;
                    }

                    String week_url_min2 =  "#bd3 > div.temperature > div.min > span";
                    String week_url_max2 = "#bd3 > div.temperature > div.max > span";
                    String week_url_subsc2 = "#bd3c > div.wDescription.clearfix > div.rSide > div";
                    String week_temp_min2 = week.select(week_url_min2).text();
                    String week_temp_max2 = week.select(week_url_max2).text();
                    String week_subsc2 = week.select(week_url_subsc2).text();
                    output_mess = output_mess +"\n" + day +"-" + month_str + " " + "min: " + week_temp_min2 + " max: " + week_temp_max2 + "." + "\n" + week_subsc2;

                    week = null;

                    day++;
                    if ((month == 1 || month == 3|| month == 5 || month == 7 || month == 8 || month == 10) && day == 32)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 31
                    }
                    if(month == 12 && day == 31)
                    {
                        day = 1;// Переход с декабря на январь
                        month = 0;
                    }
                    if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 30
                    }
                    if((year == 2020 || year == 2024 || year == 2028 || year == 2032) && day == 29 && month == 2)
                    {
                        day = 1;//Высокосный год
                        month++;//Февраль
                    }
                    if((year == 2018 || year == 2019 || year == 2021 || year == 2022 || year == 2023 || year == 2025 || year == 2026 || year == 2027 || year == 2029 || year == 2030 || year == 2031) && day == 28 && month == 2)
                    {
                        day = 1;// Не высокосный год
                        month++;// Февраль
                    }

                    if(day < 10){
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-0" + day; ;
                    }
                    else {
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-" + day;
                    }
                    try {
                        week = Jsoup.connect(url).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    switch (month){
                        case 1:
                            month_str = "Января";
                            break;
                        case 2:
                            month_str = "Февраля";
                            break;
                        case 3:
                            month_str = "Марта";
                            break;
                        case 4:
                            month_str = "Апреля";
                            break;
                        case 5:
                            month_str = "Майя";
                            break;
                        case 6:
                            month_str = "Июня";
                            break;
                        case 7:
                            month_str = "Июля";
                            break;
                        case 8:
                            month_str = "Августа";
                            break;
                        case 9:
                            month_str = "Сентября";
                            break;
                        case 10:
                            month_str = "Октября";
                            break;
                        case 11:
                            month_str = "Ноября";
                            break;
                        case 12:
                            month_str = "Декабря";
                            break;
                    }

                    String week_url_min3 =  "#bd4 > div.temperature > div.min > span";
                    String week_url_max3 = "#bd4 > div.temperature > div.max > span";
                    String week_url_subsc3 = "#bd4c > div.wDescription.clearfix > div.rSide > div";
                    String week_temp_min3 = week.select(week_url_min3).text();
                    String week_temp_max3 = week.select(week_url_max3).text();
                    String week_subsc3 = week.select(week_url_subsc3).text();
                    output_mess = output_mess +"\n" + day +"-" + month_str + " " + "min: " + week_temp_min3 + " max: " + week_temp_max3 + "." + "\n" + week_subsc3;


                    week = null;

                    day++;
                    if ((month == 1 || month == 3|| month == 5 || month == 7 || month == 8 || month == 10) && day == 32)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 31
                    }
                    if(month == 12 && day == 31)
                    {
                        day = 1;// Переход с декабря на январь
                        month = 0;
                    }
                    if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 30
                    }
                    if((year == 2020 || year == 2024 || year == 2028 || year == 2032) && day == 29 && month == 2)
                    {
                        day = 1;//Высокосный год
                        month++;//Февраль
                    }
                    if((year == 2018 || year == 2019 || year == 2021 || year == 2022 || year == 2023 || year == 2025 || year == 2026 || year == 2027 || year == 2029 || year == 2030 || year == 2031) && day == 28 && month == 2)
                    {
                        day = 1;// Не высокосный год
                        month++;// Февраль
                    }

                    if(day < 10){
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-0" + day; ;
                    }
                    else {
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-" + day;
                    }
                    try {
                        week = Jsoup.connect(url).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    switch (month){
                        case 1:
                            month_str = "Января";
                            break;
                        case 2:
                            month_str = "Февраля";
                            break;
                        case 3:
                            month_str = "Марта";
                            break;
                        case 4:
                            month_str = "Апреля";
                            break;
                        case 5:
                            month_str = "Майя";
                            break;
                        case 6:
                            month_str = "Июня";
                            break;
                        case 7:
                            month_str = "Июля";
                            break;
                        case 8:
                            month_str = "Августа";
                            break;
                        case 9:
                            month_str = "Сентября";
                            break;
                        case 10:
                            month_str = "Октября";
                            break;
                        case 11:
                            month_str = "Ноября";
                            break;
                        case 12:
                            month_str = "Декабря";
                            break;
                    }

                    String week_url_min4 =  "#bd5 > div.temperature > div.min > span";
                    String week_url_max4 = "#bd5 > div.temperature > div.max > span";
                    String week_url_subsc4 = "#bd5c > div.wDescription.clearfix > div.rSide > div";
                    String week_temp_min4 = week.select(week_url_min4).text();
                    String week_temp_max4 = week.select(week_url_max4).text();
                    String week_subsc4 = week.select(week_url_subsc4).text();
                    output_mess = output_mess +"\n" + day +"-" + month_str + " " + "min: " + week_temp_min4 + " max: " + week_temp_max4 + "." + "\n" + week_subsc4;


                    week = null;

                    day++;
                    if ((month == 1 || month == 3|| month == 5 || month == 7 || month == 8 || month == 10) && day == 32)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 31
                    }
                    if(month == 12 && day == 31)
                    {
                        day = 1;// Переход с декабря на январь
                        month = 0;
                    }
                    if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 30
                    }
                    if((year == 2020 || year == 2024 || year == 2028 || year == 2032) && day == 29 && month == 2)
                    {
                        day = 1;//Высокосный год
                        month++;//Февраль
                    }
                    if((year == 2018 || year == 2019 || year == 2021 || year == 2022 || year == 2023 || year == 2025 || year == 2026 || year == 2027 || year == 2029 || year == 2030 || year == 2031) && day == 28 && month == 2)
                    {
                        day = 1;// Не высокосный год
                        month++;// Февраль
                    }

                    if(day < 10){
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-0" + day; ;
                    }
                    else {
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-" + day;
                    }
                    try {
                        week = Jsoup.connect(url).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    switch (month){
                        case 1:
                            month_str = "Января";
                            break;
                        case 2:
                            month_str = "Февраля";
                            break;
                        case 3:
                            month_str = "Марта";
                            break;
                        case 4:
                            month_str = "Апреля";
                            break;
                        case 5:
                            month_str = "Майя";
                            break;
                        case 6:
                            month_str = "Июня";
                            break;
                        case 7:
                            month_str = "Июля";
                            break;
                        case 8:
                            month_str = "Августа";
                            break;
                        case 9:
                            month_str = "Сентября";
                            break;
                        case 10:
                            month_str = "Октября";
                            break;
                        case 11:
                            month_str = "Ноября";
                            break;
                        case 12:
                            month_str = "Декабря";
                            break;
                    }

                    String week_url_min5 =  "#bd6 > div.temperature > div.min > span";
                    String week_url_max5 = "#bd6 > div.temperature > div.max > span";
                    String week_url_subsc5 = "#bd6c > div.wDescription.clearfix > div.rSide > div";
                    String week_temp_min5 = week.select(week_url_min5).text();
                    String week_temp_max5 = week.select(week_url_max5).text();
                    String week_subsc5 = week.select(week_url_subsc5).text();
                    output_mess = output_mess +"\n" + day +"-" + month_str + " " + "min: " + week_temp_min5 + " max: " + week_temp_max5 + "." + "\n" + week_subsc5;


                    week = null;

                    day++;
                    if ((month == 1 || month == 3|| month == 5 || month == 7 || month == 8 || month == 10) && day == 32)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 31
                    }
                    if(month == 12 && day == 31)
                    {
                        day = 1;// Переход с декабря на январь
                        month = 0;
                    }
                    if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31)
                    {
                        day = 1;// Переход на следующий месяц
                        month++;// Кол-во дней = 30
                    }
                    if((year == 2020 || year == 2024 || year == 2028 || year == 2032) && day == 29 && month == 2)
                    {
                        day = 1;//Высокосный год
                        month++;//Февраль
                    }
                    if((year == 2018 || year == 2019 || year == 2021 || year == 2022 || year == 2023 || year == 2025 || year == 2026 || year == 2027 || year == 2029 || year == 2030 || year == 2031) && day == 28 && month == 2)
                    {
                        day = 1;// Не высокосный год
                        month++;// Февраль
                    }

                    if(day < 10){
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-0" + day; ;
                    }
                    else {
                        url = "https://sinoptik.ua/%D0%BF%D0%BE%D0%B3%D0%BE%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%82%D0%B0%D0%B2%D0%B0" + "/" + year + "-" + month + "-" + day;
                    }
                    try {
                        week = Jsoup.connect(url).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    switch (month){
                        case 1:
                            month_str = "Января";
                            break;
                        case 2:
                            month_str = "Февраля";
                            break;
                        case 3:
                            month_str = "Марта";
                            break;
                        case 4:
                            month_str = "Апреля";
                            break;
                        case 5:
                            month_str = "Майя";
                            break;
                        case 6:
                            month_str = "Июня";
                            break;
                        case 7:
                            month_str = "Июля";
                            break;
                        case 8:
                            month_str = "Августа";
                            break;
                        case 9:
                            month_str = "Сентября";
                            break;
                        case 10:
                            month_str = "Октября";
                            break;
                        case 11:
                            month_str = "Ноября";
                            break;
                        case 12:
                            month_str = "Декабря";
                            break;
                    }

                    String week_url_min6 =  "#bd7 > div.temperature > div.min > span";
                    String week_url_max6 = "#bd7 > div.temperature > div.max > span";
                    String week_url_subsc6 = "#bd7c > div.wDescription.clearfix > div.rSide > div";
                    String week_temp_min6 = week.select(week_url_min6).text();
                    String week_temp_max6 = week.select(week_url_max6).text();
                    String week_subsc6 = week.select(week_url_subsc6).text();
                    output_mess = output_mess +"\n" + day +"-" + month_str + " " + "min: " + week_temp_min6 + " max: " + week_temp_max6 + "." + "\n" + week_subsc6;
                    break;

                case "Назад":
                    output_mess = "Главное меню.";
                    output.setReplyMarkup(key1);
                    break;
                case "Курс валют":

                    ReplyKeyboardMarkup key_rate = new ReplyKeyboardMarkup();
                    List<KeyboardRow> key_rate2 = new ArrayList<>();
                    KeyboardRow rate_row = new KeyboardRow();
                    rate_row.add("USD");
                    rate_row.add("EUR");
                    key_rate2.add(rate_row);
                    rate_row = new KeyboardRow();
                    rate_row.add("RUB");
                    rate_row.add("GPB");
                    rate_row.add("PLN");
                    key_rate2.add(rate_row);
                    rate_row = new KeyboardRow();
                    rate_row.add("Bitcoin");
                    rate_row.add("Ethereum");
                    key_rate2.add(rate_row);
                    rate_row = new KeyboardRow();
                    rate_row.add("Назад");
                    key_rate2.add(rate_row);
                    key_rate.setKeyboard(key_rate2);
                    key_rate.setResizeKeyboard(true);
                    output.setReplyMarkup(key_rate);
                    output_mess = "Выберте валюту";
                    break;

                case "USD":
                    org.jsoup.nodes.Document rate = null;
                    try {
                        rate = Jsoup.connect("https://kurs.com.ua/").get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String rate_USD_nbu = rate.select("#elBoardSummary > table > tbody > tr:nth-child(1) > td:nth-child(8) > span.ipsKurs_rate.ipsResponsive_hidePhone").text();
                    String rate_USD_buy = rate.select("#elBoardSummary > table > tbody > tr:nth-child(1) > td:nth-child(2) > span.ipsKurs_rate").text();
                    String rate_USD_sell = rate.select("#elBoardSummary > table > tbody > tr:nth-child(1) > td:nth-child(4) > span.ipsKurs_rate").text();
                    String rate_USD_black = rate.select("#elBoardSummary > table > tbody > tr:nth-child(1) > td.ipsKursTable_rate.ipsKurs_highlight.ipsType_right > span.ipsKurs_rate.ipsResponsive_hidePhone").text();

                    output_mess = "1 USD:\n" + "Курс НБУ: " + rate_USD_nbu +" UAH." + "\nПокупка: " + rate_USD_buy +" UAH." + "\nПродажа: " + rate_USD_sell + " UAH." + "\nНа чёрном рынке: " + rate_USD_black + " UAH.";

                    break;

                case "EUR":
                    rate = null;
                    try {
                        rate = Jsoup.connect("https://kurs.com.ua/").get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String rate_EUR_nbu = rate.select("#elBoardSummary > table > tbody > tr:nth-child(2) > td:nth-child(8) > span.ipsKurs_rate.ipsResponsive_hidePhone").text();
                    String rate_EUR_buy = rate.select("#elBoardSummary > table > tbody > tr:nth-child(2) > td:nth-child(2) > span.ipsKurs_rate").text();
                    String rate_EUR_sell = rate.select("#elBoardSummary > table > tbody > tr:nth-child(2) > td:nth-child(4) > span.ipsKurs_rate").text();
                    String rate_EUR_black = rate.select("#elBoardSummary > table > tbody > tr:nth-child(2) > td.ipsKursTable_rate.ipsKurs_highlight.ipsType_right > span.ipsKurs_rate.ipsResponsive_hidePhone").text();
                    output_mess = "1 EUR:\n" + "Курс НБУ: " + rate_EUR_nbu +" UAH." + "\nПокупка: " + rate_EUR_buy +" UAH." + "\nПродажа: " + rate_EUR_sell + " UAH." + "\nНа чёрном рынке: " + rate_EUR_black + " UAH.";
                    break;

                case "RUB":
                    rate = null;
                    try {
                        rate = Jsoup.connect("https://kurs.com.ua/").get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String rate_RUB_nbu = rate.select("#elBoardSummary > table > tbody > tr:nth-child(3) > td:nth-child(8) > span.ipsKurs_rate.ipsResponsive_hidePhone").text();
                    String rate_RUB_buy = rate.select("#elBoardSummary > table > tbody > tr:nth-child(3) > td:nth-child(2) > span.ipsKurs_rate").text();
                    String rate_RUB_sell = rate.select("#elBoardSummary > table > tbody > tr:nth-child(3) > td:nth-child(4) > span.ipsKurs_rate").text();
                    String rate_RUB_black = rate.select("#elBoardSummary > table > tbody > tr:nth-child(3) > td.ipsKursTable_rate.ipsKurs_highlight.ipsType_right > span.ipsKurs_rate.ipsResponsive_hidePhone").text();
                    output_mess = "1 RUB:\n" + "Курс НБУ: " + rate_RUB_nbu +" UAH." + "\nПокупка: " + rate_RUB_buy +" UAH." + "\nПродажа: " + rate_RUB_sell + " UAH." + "\nНа чёрном рынке: " + rate_RUB_black + " UAH.";
                    break;

                case "GPB":
                    rate = null;
                    try {
                        rate = Jsoup.connect("https://kurs.com.ua/").get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String rate_GPB_nbu = rate.select("#elBoardSummary > table > tbody > tr:nth-child(4) > td:nth-child(8) > span.ipsKurs_rate.ipsResponsive_hidePhone").text();
                    String rate_GPB_buy = rate.select("#elBoardSummary > table > tbody > tr:nth-child(4) > td:nth-child(2) > span.ipsKurs_rate").text();
                    String rate_GPB_sell = rate.select("#elBoardSummary > table > tbody > tr:nth-child(4) > td:nth-child(4) > span.ipsKurs_rate").text();
                    String rate_GPB_black = rate.select("#elBoardSummary > table > tbody > tr:nth-child(4) > td.ipsKursTable_rate.ipsKurs_highlight.ipsType_right > span.ipsKurs_rate.ipsResponsive_hidePhone").text();
                    output_mess = "1 GPB:\n" + "Курс НБУ: " + rate_GPB_nbu +" UAH." + "\nПокупка: " + rate_GPB_buy +" UAH." + "\nПродажа: " + rate_GPB_sell + " UAH." + "\nНа чёрном рынке: " + rate_GPB_black + " UAH.";
                    break;

                case "PLN":
                    rate = null;
                    try {
                        rate = Jsoup.connect("https://kurs.com.ua/").get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String rate_PLN_nbu = rate.select("#elBoardSummary > table > tbody > tr:nth-child(6) > td:nth-child(8) > span.ipsKurs_rate.ipsResponsive_hidePhone").text();
                    String rate_PLN_buy = rate.select("#elBoardSummary > table > tbody > tr:nth-child(6) > td:nth-child(2) > span.ipsKurs_rate").text();
                    String rate_PLN_sell = rate.select("#elBoardSummary > table > tbody > tr:nth-child(6) > td:nth-child(4) > span.ipsKurs_rate").text();
                    String rate_PLN_black = rate.select("#elBoardSummary > table > tbody > tr:nth-child(6) > td.ipsKursTable_rate.ipsKurs_highlight.ipsType_right > span.ipsKurs_rate.ipsResponsive_hidePhone").text();
                    output_mess = "1 PLN:\n" + "Курс НБУ: " + rate_PLN_nbu +" UAH." + "\nПокупка: " + rate_PLN_buy +" UAH." + "\nПродажа: " + rate_PLN_sell + " UAH." + "\nНа чёрном рынке: " + rate_PLN_black + " UAH.";
                    break;

                case "Bitcoin":
                    org.jsoup.nodes.Document rate_bit = null;
                    try {
                        rate_bit = Jsoup.connect("https://maanimo.com/cryptocurrency/bitcoin").get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String rate_Bitcoin_buy = rate_bit.select("#crypto > div:nth-child(3) > table > tbody > tr > td:nth-child(4)").text();
                    String rate_Bitcoin_sell = rate_bit.select("#crypto > div:nth-child(3) > table > tbody > tr > td:nth-child(5)").text();
                    output_mess = "1 Bitcoin:\n"+ "Покупка: " + rate_Bitcoin_buy + " USD." + "\nПродажа: "+ rate_Bitcoin_sell + " USD.";
                    break;

                case "Ethereum":
                    org.jsoup.nodes.Document rate_ethe = null;
                    try {
                        rate_ethe = Jsoup.connect("https://www.fxclub.org/markets/crypto/ethereum/").get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String rate_Ethe_price = rate_ethe.select("body > div.container > main > div.page-content.clearfix > div.fxc-instrument-info > div.fxc-instrument-data > div.fxc-instrument-rate > output").text();
                    String rate_Ethe_gain = rate_ethe.select("body > div.container > main > div.page-content.clearfix > div.fxc-instrument-info > div.fxc-instrument-data > div.fxc-instrument-change > output").text();
                    output_mess = "1 Ethereum = " + rate_Ethe_price + " USD." + "\nПрирост за день: " + rate_Ethe_gain + ".";
                    break;

                case "none":
                    output_mess = "Функция еще в разработке";
                    break;
                case "Настройки":
                    output_mess = "Функция еще в разработке";
                    break;
                case "Аня, как дела?":
                    output_mess = "У меня всё отлично, но главное что бы у тебя все было хорошо.";
                    break;
                case "Ты кто?":
                    output_mess = "Я бот помошник Аня. Меня создал один смышлёный парень для того что бы помогать людям.";
                    break;
                case "Аня":
                    output_mess = "Что ты хотел," +user_name + "?";
                    break;
                case "Помоги":
                    output_mess = "Отстань у меня нет настроения...";
                    break;
            }


            output.setChatId(chat_id);
            output.setText(output_mess);

            try{
                sendMessage(output);
            } catch (TelegramApiException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername(){
        return "AnnaGr_bot";
    }

    @Override
    public String getBotToken(){
        return "371570182:AAHSnkwbrK2Y6cLBwG9tuo81cEXuxMq2Wtc";
    }

}

