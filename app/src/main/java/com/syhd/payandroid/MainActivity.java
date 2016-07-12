package com.syhd.payandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.syhd.payandroid.alipay.client.Alipay;
import com.syhd.payandroid.alipay.server.AlipayServer;
import com.syhd.payandroid.net.HttpUtils;
import com.syhd.payandroid.net.StringCallback;
import com.syhd.payandroid.weixin.WXPay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private Button alipayBtn;
    private Button wxpayBtn;

    /**
     * 替换自己的支付宝回调地址
     */
    private String alipaycallback = "http://callbackurl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 支付宝支付
         */
        alipayBtn = (Button) findViewById(R.id.alipay);
        alipayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //在服务器生成订单信息
                String payInfo = AlipayServer.getPayInfo(getOutTradeNo(), "商品名称", "商品描述", "1", alipaycallback);
                //客户端调起支付宝支付
                Alipay alipay = new Alipay(MainActivity.this, payInfo, new Alipay.AlipayResultCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "支付成功");
                    }

                    @Override
                    public void onDealing() {
                        Log.d(TAG, "支付中");
                    }

                    @Override
                    public void onError(int error_code) {
                        Log.d(TAG, "支付错误" + error_code);
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "支付取消");
                    }
                });
                alipay.doPay();
            }
        });

        /**
         * 微信支付
         * 微信支付常见坑
         * 1.微信开放平台的包名和签名是否和本地的一致
         * 2.服务器能拿到prepare_id,还是返回-1，查看调起支付接口时的签名是否计算正确
         * 3.能调起支付，没有返回消息的，请查看自己项目包下是否有（wxapi.WXPayEntryActivity）
         * 4.本地调试时一定要使用正式签名文件进行调试，否则是调不起微信支付窗口的
         * 5.网络上遇到说微信缓存会影响返回-1的，目前没有遇到过
         */
        wxpayBtn = (Button) findViewById(R.id.wxpay);
        wxpayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**请求自己的服务器获取支付参数*/
                String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";

                /**
                 * {"appid":"wxb4ba3c02aa476ea1","partnerid":"1305176001","package":"Sign=WXPay","noncestr":"d86b800063efb51837c1fd6f6c806acc","timestamp":1468313097,"prepayid":"wx20160712164457dddf6add490242369966","sign":"E6FFA6E8A39589FE4C5A51592095398A"}
                 */
                HttpUtils.httpGet(MainActivity.this, url,  new StringCallback() {
                    @Override
                    public void onError(String request) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        WXPay wxpay = new WXPay(MainActivity.this, "wxb4ba3c02aa476ea1");
                        wxpay.doPay(response, new WXPay.WXPayResultCallBack() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "支付成功");
                            }

                            @Override
                            public void onError(int error_code) {
                                Log.d(TAG, "支付失败" + error_code);
                            }

                            @Override
                            public void onCancel() {
                                Log.d(TAG, "支付取消");
                            }
                        });
                    }
                });
            }
        });

    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
}
