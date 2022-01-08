package com.github.funczz.kotlin.extensions.stdlib;

/**
 * メッセージ・リスナー
 */
public interface MessageListener extends java.util.EventListener {

    void performed(MessageEvent v);

}