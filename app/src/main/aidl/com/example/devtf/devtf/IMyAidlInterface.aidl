// IMyAidlInterface.aidl
package com.example.devtf.devtf;

// Declare any non-default types here with import statements
import com.example.devtf.devtf.Message;
interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

  Message sayhello();
}
