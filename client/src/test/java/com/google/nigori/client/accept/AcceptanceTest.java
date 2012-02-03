/*
 * Copyright (C) 2012 Daniel Thomas (drt24)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.nigori.client.accept;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.nigori.client.HTTPNigoriDatastore;
import com.google.nigori.client.NigoriCryptographyException;
import com.google.nigori.client.NigoriDatastore;
import com.google.nigori.client.SyncingNigoriDatastore;

/**
 * @author drt24
 * 
 */
@RunWith(Parameterized.class)
public class AcceptanceTest {
  public static final byte[] NULL_DELETE_TOKEN = new byte[]{};

  @Parameters
  public static Collection<DatastoreFactory[]> stores() {
    return Arrays.asList(new DatastoreFactory[][] {{new HTTPDatastoreFactory()}, {new SyncingDatastoreFactory()}});
  }

  private DatastoreFactory datastore;

  public AcceptanceTest(DatastoreFactory store) {
    this.datastore = store;
  }

  protected NigoriDatastore getStore() throws NigoriCryptographyException, IOException {
    return datastore.makeDatastore();
  }

  protected interface DatastoreFactory {
    public NigoriDatastore makeDatastore() throws UnsupportedEncodingException,
        NigoriCryptographyException, IOException;
  }
  private static class HTTPDatastoreFactory implements DatastoreFactory {
    public NigoriDatastore makeDatastore() throws UnsupportedEncodingException,
        NigoriCryptographyException {
      return new HTTPNigoriDatastore(AcceptanceTests.HOST, AcceptanceTests.PORT, AcceptanceTests.PATH);
    }
  }
  private static class SyncingDatastoreFactory implements DatastoreFactory {
    public NigoriDatastore makeDatastore() throws NigoriCryptographyException, IOException {
      return new SyncingNigoriDatastore(
          new HTTPNigoriDatastore(AcceptanceTests.HOST, AcceptanceTests.PORT, AcceptanceTests.PATH),
          new HTTPNigoriDatastore(AcceptanceTests.HOST, AcceptanceTests.PORT, AcceptanceTests.PATH));
    }
  }
}
