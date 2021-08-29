import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.util.ArrayList;


/**
 * @create 27.08.2021 23:53
 */
public class main_Bot extends TelegramLongPollingBot {
    /**
     *
     * https://core.telegram.org/bots
     * https://core.telegram.org/bots/api
     * https://qna.habr.com/q/967189
     * https://nationalbank.kz/ru/exchangerates/ezhednevnye-oficialnye-rynochnye-kursy-valyut
     * https://stackoverflow.com/questions/7421612/slf4j-failed-to-load-class-org-slf4j-impl-staticloggerbinder
     *
     */

    static ArrayList<String> getByTable_Date = new ArrayList<>();
    static ArrayList<String> getByTable_USD = new ArrayList<>();
    static ArrayList<String> getByTable_EUR = new ArrayList<>();
    static ArrayList<String> getByTable_RUB = new ArrayList<>();

    public static void main(String[] args) throws TelegramApiException {
        parse_jsoup("20.08.2021", "29.08.2021");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(new main_Bot());
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public static void parse_jsoup(String begin, String end){
        String url_national_bank = "https://nationalbank.kz/ru/exchangerates/ezhednevnye-oficialnye-rynochnye-kursy-valyut/report?rates%5B%5D=5&rates%5B%5D=6&rates%5B%5D=16&beginDate=" + begin + "&endDate=" + end;

        try {
            Document doc = Jsoup.connect(url_national_bank).get();
            Element table = doc.select("tbody").get(0);
            Elements rows = table.select("tr");

            for (Element row : rows) {
                Elements columns = row.select("td");
                getByTable_Date.add(columns.get(0).text());
                getByTable_USD.add(columns.get(2).text());
                getByTable_EUR.add(columns.get(4).text());
                getByTable_RUB.add(columns.get(6).text());
            }
        } catch (IOException exception) { //'org.jsoup.HttpStatusException' is a subclass of 'java.io.IOException'
            exception.printStackTrace();
        }
    }

    public void sendMessage(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try{
            backend_buttons.setButtons_first(sendMessage);
            execute(sendMessage);
        }catch (TelegramApiException | IOException e){
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            System.out.println(message.getText());

            if (message.getText().equals("/start")) {
                sendMessage(message, "Hello. I am Freedom Finance Adil bot." +
                        "\nWith my help, you can view the history of changes in exchange rates (USD, EUR, RUB) in KZT for 10 days."); // * _
            }

            else if (message.getText().equals("About Creator")) {
                sendMessage(message, "Myktybek Adil");
            }

            else {
                String currency = message.getText();
                sendMessage(message, "At your request");

                StringBuilder sb=new StringBuilder();
                switch (currency){
                    case "USD":
                        for (int i=0; i<getByTable_Date.size();i++){
                            sb.append(getByTable_Date.get(i)).append(":  ").append(getByTable_USD.get(i));
                            sb.append("\n");
                        }
                        break;
                    case "EUR":
                        for (int i=0; i<getByTable_Date.size();i++){
                            sb.append(getByTable_Date.get(i)).append(":  ").append(getByTable_EUR.get(i));
                            sb.append("\n");
                        }
                        break;
                    case "RUB":
                        for (int i=0; i<getByTable_Date.size();i++){
                            sb.append(getByTable_Date.get(i)).append(":  ").append(getByTable_RUB.get(i));
                            sb.append("\n");
                        }
                        break;
                    default:
                        sb.append("Select currency");
                }

                String answer = sb.toString();
                sb.setLength(0);
                sendMessage(message, answer);
            }
        }
    }

    public String getBotUsername() {
        return "Freedom_Finance_Adil_bot";
    }

    public String getBotToken() {
        return "1964504177:AAGzyC65lGx6rsQiIKTFi74dSoxoZTur1RA";
    }
}