package com.bestplaces.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyTelegramBot extends TelegramLongPollingBot {
    @Autowired
    private VerificationCodeService verificationCodeService;

    @Override
    public void onUpdateReceived(Update update) {
//      if (update.hasMessage() && update.getMessage().hasText()) {
//            String messageText = update.getMessage().getText();
//            String chatId = String.valueOf(update.getMessage().getChatId());
//            // Kiểm tra nếu tin nhắn là lệnh /chatid
//            if ("/start".equals(messageText)) {
//                // Trả lời với chatId của trò chuyện
//                sendChatId(chatId);
//            }
//        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = String.valueOf(update.getMessage().getChatId());

            // Kiểm tra nếu tin nhắn là lệnh /phonenumber
            if ("/phonenumber".equals(messageText)) {
                // Gửi yêu cầu chia sẻ số điện thoại
                sendRequestPhoneNumber(chatId);
            } else {
                // Xử lý các lệnh khác ở đây
            }
        }
        Message message = update.getMessage();
        // Lấy thông tin về số điện thoại từ đối tượng Contact
        Contact contact = message.getContact();
        String phoneNumber = contact.getPhoneNumber();

        // In ra số điện thoại để kiểm tra
        System.out.println("Số điện thoại của người dùng: " + phoneNumber);
    }

    private void sendRequestPhoneNumber(String chatId) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Vui lòng chia sẻ số điện thoại của bạn.");

        // Tạo một nút bấm cho việc chia sẻ số điện thoại và đặt thuộc tính request_contact trực tiếp
        KeyboardButton contactButton = new KeyboardButton("Chia sẻ số điện thoại");
        contactButton.setRequestContact(true);

        // Tạo một hàng của bàn phím với nút bấm đã tạo
        KeyboardRow row = new KeyboardRow();
        row.add(contactButton);

        // Tạo một danh sách các hàng của bàn phím
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        // Tạo một bàn phím tương tác với danh sách các hàng đã tạo
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(keyboard);

        // Gán bàn phím tương tác vào tin nhắn
        sendMessage.setReplyMarkup(keyboardMarkup);

        // Gán bàn phím tương tác vào tin nhắn
        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            // Gửi tin nhắn yêu cầu chia sẻ số điện thoại đến người dùng
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        // Trả về tên người dùng của bot của bạn
        return "BestPlacess_bot";
    }

    @Override
    public String getBotToken() {
        // Trả về token truy cập API của bot của bạn
        return "6397106089:AAHxr8VWtNrIBiPY9dNNQ4_iEW1OzWDwaaw";
    }

    public void sendVerificationCode(String phoneNumber) {
        SendMessage message = new SendMessage();
        String verificationCodes = verificationCodeService.generateVerificationCode(verificationCodeService.UserNameAtPresent());
        message.setChatId(phoneNumber); // Số điện thoại của người dùng
        message.setText("Your verification code is: " + verificationCodes);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}

