package kr.heythisway.conmgr;

import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 기어코 메인 스레드에서 네트워크 입출력을 하려면
        스트릭트 모드를 약간 풀어주면 가능은 하다.*/
        StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(pol);

        Button down = (Button) findViewById(R.id.down);
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String html;
                html = DownloadHtml("http://www.google.com");
                TextView result = (TextView) findViewById(R.id.result);
                result.setText(html);
            }
        });
    }

    private String DownloadHtml(String addr) {
        // 리턴을 받는 문서가 굉장히 길 수 있으므로 StringBuilder를 사용하는 것이 효율적이다.
        StringBuilder html = new StringBuilder();
        try {
            // 인수로 전달받은 주소로부터 URL객체를 생성한다.
            URL url = new URL(addr);
            // openConnection 메서드로 연결
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection != null) {
                // 연결 타임아웃을 10초로 지정
                connection.setConnectTimeout(10000);
                // 캐쉬는 사용하지 않음
                connection.setUseCaches(false);
                // getResponseCode 메서드로 요청을 보내고요청이 정상적으로 리턴되면
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // 입력 스트림으로부터 버퍼리더로 HTML 문서를 읽어들인다.
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    for (; ; ) {
                        String line = br.readLine();    // readLine 메서드는 줄 단위로 읽되 개행 코드는 읽지 않는다.
                        if (line == null) {
                            break;
                        }
                        html.append(line + '\n');
                    }
                    br.close();
                }
                connection.disconnect();
            }
        } catch (NetworkOnMainThreadException e) {
            return "Error : 메인 스레드 네트워크 작업 에러- " + e.getMessage();
        } catch (Exception e) {
            return "Error : " + e.getMessage();
        }
        return html.toString();
    }
}
