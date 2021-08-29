import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @create 28.08.2021 0:04
 */
public class backend_buttons {

    public static void setButtons_first(SendMessage sendMessage) throws IOException {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> ButtonsRowsList = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(new KeyboardButton("USD"));
        firstRow.add(new KeyboardButton("EUR"));
        firstRow.add(new KeyboardButton("RUB"));
        ButtonsRowsList.add(firstRow);

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(new KeyboardButton("About Creator"));
        ButtonsRowsList.add(secondRow);

        replyKeyboardMarkup.setKeyboard(ButtonsRowsList);
    }
}