/*
 * Mark Bridge (markbridge.com), San Francisco CA 94102, j2eewebtier@gmail.com 
 * Copyright (c) 2012 Mark Bridge All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.markbridge.application.boot;

/**
 *
 * @author mbridge
 */
public class App {
    
    public static void main( String[] args ) {
        
        System.out.println("ENV_ENCRYPTION_PWD=password");
        System.out.println(System.getenv("ENV_ENCRYPTION_PWD").equals("password") + "\n");
        
        //Boot.init();
        System.out.println("Boot: " + Boot.encrypt("Boot: Hello world!"));
        System.out.println("Boot: " + Boot.decrypt("rsbcrh9WoF8UwDrMQDzlQ7Tg8S6gz1ov") + "\n");
        
        System.out.println("Config: " + Config.getString("jar.name"));
        System.out.println("Config: " + Config.getString("dev.hello") + "\n");
        
        System.out.println("Res: " + Res.getString("cleartext.greeting"));
        System.out.println("Res: " + Res.getString("ciphertext.greeting"));
        
    }
}
