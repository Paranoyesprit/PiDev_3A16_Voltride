package Entities;
import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;
public class SendSms {
    public static final String ACCOUNT_SID = "ACc51bccccd69cb574e2b382ec326f8651";
    public static final String AUTH_TOKEN = "b5ee6b81b973ea1e323974959a54ef5c";

    public static void SendSms(Reservation_b r,String messages,String num) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        messages=messages+r.date_d+"et se termine :"+r.date_f+"id:"+r.getId_r();
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(num),
                        new com.twilio.type.PhoneNumber("+15855222234"),
                        messages)
                .create();

        System.out.println(message.getSid());
    }
}
