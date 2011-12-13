/*
 * Copyright (C) 2011 Daniel Thomas (drt24)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.nigori.client;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.google.nigori.common.MessageLibrary;

public abstract class KeyManagerTest {

  protected abstract KeyManager getKeyManager(byte[] serverName, byte[] userName, byte[] password) throws NigoriCryptographyException;
  protected abstract KeyManager getKeyManager(byte[] serverName) throws NigoriCryptographyException;

  @Test
  public void getUsernameAndPassword() throws NigoriCryptographyException, UnsupportedEncodingException {
    byte[] serverName = "serverName".getBytes(MessageLibrary.CHARSET);
    byte[] userName = "userName".getBytes(MessageLibrary.CHARSET);
    byte[] password = "password".getBytes(MessageLibrary.CHARSET);
    KeyManager keyManger = getKeyManager(serverName, userName, password);
    assertArrayEquals("Username different",userName,keyManger.getUsername());
    assertArrayEquals("Username different",password,keyManger.getPassword());
  }
  @Test
  public void decryptReversesEncrypt() throws UnsupportedEncodingException, NigoriCryptographyException {
    KeyManager keyManager = getKeyManager("server".getBytes(MessageLibrary.CHARSET));
    byte[] plaintext = "plaintext".getBytes(MessageLibrary.CHARSET);
    assertArrayEquals(plaintext,keyManager.decrypt(keyManager.encrypt(plaintext)));
  }
  @Test
  public void encryptNotIdentity() throws UnsupportedEncodingException, NigoriCryptographyException{
    KeyManager keyManager = getKeyManager("server".getBytes(MessageLibrary.CHARSET));
    byte[] plaintext = "plaintext".getBytes(MessageLibrary.CHARSET);
    assertThat(plaintext, not(equalTo(keyManager.encrypt(plaintext))));
  }
  @Test
  public void encryptSameValueGivesDifferentAnswers() throws UnsupportedEncodingException, NigoriCryptographyException{
    KeyManager keyManager = getKeyManager("server".getBytes(MessageLibrary.CHARSET));
    byte[] plaintext = "plaintext".getBytes(MessageLibrary.CHARSET);
    assertThat(keyManager.encrypt(plaintext), not(equalTo(keyManager.encrypt(plaintext))));
  }
  @Test
  public void encryptDeterministicallySameValueGivesSameAnswer() throws UnsupportedEncodingException, NigoriCryptographyException{
    KeyManager keyManager = getKeyManager("server".getBytes(MessageLibrary.CHARSET));
    byte[] plaintext = "plaintext".getBytes(MessageLibrary.CHARSET);
    assertThat(keyManager.encryptDeterministically(plaintext), equalTo(keyManager.encryptDeterministically(plaintext)));
  }
}
