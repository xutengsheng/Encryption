package com.xts.encryption;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xts.encryption.util.Aes;
import com.xts.encryption.util.Des;
import com.xts.encryption.util.RSACrypt;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mTvResult;
    private boolean isDes;
    private boolean isAes;
    private boolean isRsa;
    private String content;
    private String key;
    private String desEncrypt;
    private String aesEncrypt;
    private String privateKey;
    private String publicKey;
    private byte[] encryptByPrivateKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = "核武器机密文件信息";
        key = "12345678";//对称加密密码,DES密码为8个字符（最后一个不参与加密计算，只作为校验码），64位，
        initView();
        genKayPair();
    }

    /**生成秘钥对*/
    private void genKayPair() {
        try {
            Map<String, Object> genKeyPair = RSACrypt.genKeyPair();
            privateKey = RSACrypt.getPrivateKey(genKeyPair);
            publicKey = RSACrypt.getPublicKey(genKeyPair);

            showLog("privateKey="+privateKey);
            showLog("publicKey="+publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showLog(String msg){
        Log.i("result", ""+msg);
    }

    /**
     * des加密和解密
     *
     * @param v
     */
    public void des(View v) {
        try {
            if (!isDes) {//加密
                desEncrypt = Des.encrypt(content, key);
                mTvResult.setText("DES加密：" + desEncrypt);
            } else {//解密
                String decrypt = Des.decrypt(desEncrypt, key);
                mTvResult.setText("DES解密：" + decrypt);
            }

            isDes = !isDes;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**AES加密和解密*/
    public void aes(View v){
        if(!isAes){//加密
            aesEncrypt = Aes.encrypt(content, key);
            mTvResult.setText("AES加密："+aesEncrypt);
        }else{//解密
            String decrypt = Aes.decrypt(aesEncrypt, key);
            mTvResult.setText("AES解密："+decrypt);
        }
        isAes = !isAes;
    }

    /**非对称加密RSA：加密和解密*/
    public void rsa(View v){
        try {
            if(!isRsa){//加密
                encryptByPrivateKey = RSACrypt.encryptByPrivateKey(content.getBytes(), privateKey);
                //mTvResult.setText("RSA加密："+new String(encryptByPrivateKey));
                mTvResult.setText("RSA加密：" + RSACrypt.encode(encryptByPrivateKey));
            }else{//解密
                byte[] bytes = RSACrypt.decryptByPublicKey(encryptByPrivateKey, publicKey);
                mTvResult.setText("RSA解密："+new String (bytes));
            }

            isRsa = !isRsa;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mTvResult = (TextView) findViewById(R.id.tv_result);
    }
}
