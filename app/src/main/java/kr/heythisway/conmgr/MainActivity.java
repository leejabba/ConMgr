package kr.heythisway.conmgr;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * # 28.1.4 비동기 다운로드
 *
 * 만약 웹페이지를 포맷팅해서 출력하고 싶다면 네트워크 관련 코드를
 * 작성할 필요 없이 WebView 위젯을 사용하는 것이
 * 훨씬 간편하다.
 */

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button down = (Button) findViewById(R.id.down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 프로그레스 처리 과정을 대화상자로 알려준다. (Context, 창 제목, 내용)
                progressDialog = ProgressDialog.show(MainActivity.this, "기다리세요", "다운로드 중입니다...");
                // 스레드로 다운로드 받을 대상 주소를 넘긴다.
                DownThread thread = new DownThread("http://www.google.com");
                thread.start();
            }
        });
    }


    private class DownThread extends Thread {
        String addr;

        DownThread(String addr) {
            this.addr = addr;
        }

        public void run() {
            // 인자로 받은 주소의 HTML 문서를 다운로드 받는 메서드 호출
            String result = DownloadHtml.downloadHtml(addr);
            /* 스레드는 메인스레드의 위젯을 직접 건드릴 수 없기 때문에 반드시 핸드러로 메시지를 보내야 한다.
            다운로드가 완료되면 결과를 메시지의 obj에 실어 핸들러로 보내고 스레드는 종료시킨다. */
            Message message = mAfterDown.obtainMessage();
            message.obj = result;
            mAfterDown.sendMessage(message);
        }
    }


    Handler mAfterDown = new Handler() {
        public void handleMessage(Message msg) {
            // 프로그래스 대화상자를 닫는다.
            progressDialog.dismiss();
            // 다운로드 받은 응답결과를 텍스트 뷰에 출력한다.
            TextView result = (TextView) findViewById(R.id.result);
            result.setText(msg.obj + "");
        }
    };
}
