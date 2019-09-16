/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lerserialbalanca.persistence;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lerserialbalanca.models.Motorista;
import lerserialbalanca.models.Registro;

/**
 *
 * @author Desenvolvimento
 */
public class Acoes {
    
    public Motorista procurarPlaca (String placa) throws ClassNotFoundException, SQLException{
        Conexao conexao = new Conexao();
        Motorista mot = new Motorista();
        PreparedStatement sql = conexao.getConexao().prepareStatement("SELECT * from motorista where placa = ?");
        sql.setString(1, placa);
        ResultSet result = sql.executeQuery();     
        if(result.next()){
            mot.setPlaca(result.getString("placa"));
            mot.setNome(result.getString("nome"));
            mot.setFornecedor(result.getString("fornecedor"));
            mot.setProduto(result.getString("produto"));
            mot.setStatus(result.getString("status"));
            if(result.wasNull()){
                mot = null;
            }
        }
        
        sql.close();
        return mot;
    }
    
    public boolean CadastrarMotorista(Motorista mot) throws ClassNotFoundException, SQLException{
        Conexao conexao = new Conexao();
        //Motorista mot = new Motorista();
        PreparedStatement sql = conexao.getConexao().prepareStatement("INSERT INTO motorista (placa, nome, status, fornecedor, produto)"
                + "VALUES (?, ?, ?, ?, ?)");
        sql.setString(1, mot.getPlaca());
        sql.setString(2, mot.getNome());
        sql.setString(3, "S");
        sql.setString(4, mot.getFornecedor());
        sql.setString(5, mot.getProduto());  
        int registros = sql.executeUpdate();
        sql.close();
        return (registros == 1);
    }
    
    public boolean editarMotorista(Motorista mot) throws ClassNotFoundException, SQLException{
        Conexao conexao = new Conexao();
        PreparedStatement sql = conexao.getConexao().prepareStatement("UPDATE  motorista SET nome = ? , fornecedor = ?, produto = ?, status = ? WHERE placa = ?");
        sql.setString(1, mot.getNome());
        sql.setString(2, mot.getFornecedor());
        sql.setString(3, mot.getProduto());
        sql.setString(4, (mot.getStatus().equals("E"))? "S" : "E");
        sql.setString(5, mot.getPlaca());
        
        int registros = sql.executeUpdate();
        sql.close();
        return (registros == 1);
    }
    
    public List<Motorista> listarMotoristas() throws ClassNotFoundException, SQLException{
        Conexao conexao = new Conexao();
        //Motorista mot = new Motorista();
        PreparedStatement sql = conexao.getConexao().prepareStatement("SELECT * from motorista");
        ResultSet result = sql.executeQuery();
        List<Motorista> motoristas = new ArrayList<Motorista>();
        while(result.next()){
            Motorista mot = new Motorista(result.getString("placa"),result.getString("nome"),result.getString("fornecedor"), result.getString("produto"), result.getString("status"));
            motoristas.add(mot);
        }
        return motoristas;
    }
    
    
    public boolean entradaRegistro(Registro reg) throws ClassNotFoundException, SQLException{
        Conexao conexao = new Conexao();
        PreparedStatement sql = conexao.getConexao().prepareStatement("INSERT INTO registro (placa, nome, produto, fornecedor, data_entrada, hora_entrada, peso_entrada)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?)");
        sql.setString(1, reg.getPlaca());
        sql.setString(2, reg.getNome());
        sql.setString(3, reg.getProduto());
        sql.setString(4, reg.getFornecedor());
        sql.setString(5, reg.getDt_entrada());
        sql.setString(6, reg.getH_entrada());
        sql.setString(7, reg.getPs_entrada());  
        int registros = sql.executeUpdate();
        sql.close();
        return (registros == 1);
    }
    
    
    public boolean saidaRegistro(Registro reg) throws ClassNotFoundException, SQLException{
        Conexao conexao = new Conexao();
        PreparedStatement sql = conexao.getConexao().prepareStatement("UPDATE registro SET data_saida = ?, hora_saida = ?, peso_saida = ?, peso_liquido = ? where id = (SELECT id FROM registro where placa = ? order by id desc limit 1)");
        sql.setString(1, reg.getDt_saida());
        sql.setString(2, reg.getH_saida());
        sql.setString(3, reg.getPs_saida());
        sql.setString(4, reg.getPs_liquido());
        sql.setString(5, reg.getPlaca());
        int registros = sql.executeUpdate();
        sql.close();
        return (registros == 1);
    }
    
    public Registro getUltimoRegistro(String placa) throws ClassNotFoundException, SQLException{
        Conexao conexao = new Conexao();
        Registro reg = new Registro();
        PreparedStatement sql = conexao.getConexao().prepareStatement("SELECT * FROM registro where id = (SELECT id FROM registro where placa = ? order by id desc limit 1)");
        sql.setString(1, placa);
        ResultSet result = sql.executeQuery();
        while(result.next()){
            reg.setId(result.getInt("id"));
            reg.setPlaca(result.getString("placa"));
            reg.setDt_entrada(result.getString("data_entrada"));
            reg.setH_entrada(result.getString("hora_entrada"));
            reg.setPs_entrada(result.getString("peso_entrada"));
            reg.setDt_saida(result.getString("data_saida"));
            reg.setH_saida(result.getString("hora_saida"));
            reg.setPs_saida(result.getString("peso_saida"));
            reg.setPs_liquido(result.getString("peso_liquido"));
        }
        return reg;
    }
    
    public List<Registro> listarRegistros() throws ClassNotFoundException, SQLException{
        Conexao conexao = new Conexao();
        PreparedStatement sql = conexao.getConexao().prepareStatement("SELECT * from registro");
        ResultSet result = sql.executeQuery();
        List<Registro> registros = new ArrayList<Registro>();
        while(result.next()){
            Registro reg = new Registro(
                    result.getInt("id"),
                    result.getString("placa"),
                    result.getString("nome"),
                    result.getString("produto"),
                    result.getString("fornecedor"),
                    result.getString("data_entrada"),
                    result.getString("hora_entrada"),
                    result.getString("peso_entrada"),
                    result.getString("data_saida"),
                    result.getString("hora_saida"),
                    result.getString("peso_saida"),
                    result.getString("peso_liquido")
            );
            registros.add(reg);
        }
        return registros;
    }
}
