package Main;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;

public class Example {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "ACc51bccccd69cb574e2b382ec326f8651";
    public static final String AUTH_TOKEN = "1dd5858c265bc36c14379136b2f35c50";

    public static void SendSms(String messages,String num) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(num),
                        new com.twilio.type.PhoneNumber("+15855222234"),
                        messages)
                .create();

        System.out.println(message.getSid());
    }
}