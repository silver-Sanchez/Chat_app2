package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {

    private static int  SIGN_IN_CODE = 1;
    private RelativeLayout activity_main;
    private FirebaseListAdapter<Message> adapter;
    private FloatingActionButton sendBtn;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // проверка авторизации
        if(requestCode == SIGN_IN_CODE){
            if (resultCode == RESULT_OK) {//если пользователь успешно авторизовался
                Snackbar.make(activity_main, "привет", Snackbar.LENGTH_LONG).show(); // выводим сообщение
                displayAllmessages();
            } else {
                Snackbar.make(activity_main, "что то пошло не так", Snackbar.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = findViewById(R.id.activity_main); // функция позволяет находить айди объекта
        sendBtn = findViewById(R.id.buttonSend);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //тут прописываем весь функционал который должен срабатывать при нажатии на кнопку

                EditText textField = findViewById(R.id.messageField);
                if (textField.getText().toString()=="")
                    return;
                FirebaseDatabase.getInstance().getReference().push().setValue(
                        new Message
                                (FirebaseAuth.getInstance().getCurrentUser().getEmail(), textField.getText().toString()));//родключаемся и методом пуш отправляем в бд что то

                textField.setText("");
            }
        });

        //если пользователь ещё не авторизован
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE); // фунцция startActivityForResult помогает авторизовать пользов
        else {
            Snackbar.make(activity_main, "вы авторизовались", Snackbar.LENGTH_SHORT).show();
            displayAllmessages();
        }
    }

    private void displayAllmessages() {
        ListView listOfMessages = findViewById(R.id.list_of_message);

        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()) { //R.layout.list_item обращение к файлу
            @Override                                                                               //FirebaseDatabase.getInstance().getReference()) подключение к бд
            protected void populateView(View v, Message model, int position) {

                TextView mess_user, mess_time, message_text;
                mess_user = v.findViewById(R.id.message_user);
                mess_time = v.findViewById(R.id.message_time);
                message_text = v.findViewById(R.id.text_message);

                mess_user.setText(model.getUsername());  //обращаемся к классу Message обращаемся к model и берём из него Username (getUsername) выставляем в качестве текста mess_user
                mess_time.setText(DateFormat.format("dd-mm-yyyy HH:mm:ss", model.getMessagetime()));
                message_text.setText(model.getTextmessage());

            }
        };
        listOfMessages.setAdapter(adapter); // устанавливаем в окно объект адаптер
    }
}