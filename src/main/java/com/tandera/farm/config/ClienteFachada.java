/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tandera.farm.config;

import java.sql.Connection;
import org.openswing.swing.mdi.client.ClientFacade;

/**
 *
 * @author sergio.feitosa
 */
public class ClienteFachada implements ClientFacade {

    private Connection conn = null;

    public ClienteFachada(Connection conn) {
        this.conn = conn;
    }
}
