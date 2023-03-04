package com.sdnc.common.conf;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import com.sdnc.common.constant.HeaderConstant;
import com.sdnc.common.kits.AESKit;
import com.sdnc.common.kits.RSAKit;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 *
 * 使用AES解密Request Body
 *
 */
public class DecodeRequestBodyWrapper extends HttpServletRequestWrapper {

	// 已经解密完成的InputStream在后续处理中使用
	private ServletInputStream decodeInputStream;

	public DecodeRequestBodyWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return super.getReader();
	}

	/**
	 * 1、拿到request body的 input stream
	 * 2、将输入流转成字符串（用AES加密的密文字符串）
	 * 3、拿到头信息里的AES秘钥（用RSA加密的AES秘钥）
	 * 4、用RSA解密AES秘钥，获得AES秘钥明文
	 * 5、用AES秘钥解密密文body
	 * 6、将明文转成ServletInputStream
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (Objects.isNull(decodeInputStream)) {
			final ServletInputStream inputStream = super.getInputStream();
			if (Objects.isNull(inputStream)) {
				return null;
			}
			String ciphertextBody = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
			final String aesSign = getHeader(HeaderConstant.AES_SIGN);
			final String aseKey = RSAKit.decode(aesSign);
			final String plaintextBody = AESKit.decode(aseKey, ciphertextBody);
			if (StringUtils.hasText(plaintextBody)) {
				final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
						plaintextBody.getBytes(StandardCharsets.UTF_8));
				decodeInputStream = new ServletInputStream() {
					@Override
					public boolean isFinished() {
						return false;
					}

					@Override
					public boolean isReady() {
						return false;
					}

					@Override
					public void setReadListener(ReadListener listener) {
					}

					@Override
					public int read() {
						return byteArrayInputStream.read();
					}
				};
			}
		}

		return decodeInputStream;
	}

}