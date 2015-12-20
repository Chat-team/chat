package chat.ws;

import chat.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Dongfang on 2015/12/17.
 */
public class Message {

    private int messageid;
    private String sender;
    private String content;
    private String target;
    private String ctime;

    private String subject;

    public Message(String target, String content) {
        this.target = target;
        this.content = content;
        this.ctime = "" + System.currentTimeMillis();
    }

    public void setMessageID(int messageid) {
        this.messageid = messageid;
    }

    public int getID() {
        return this.messageid;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return this.target;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getCtime() {
        return this.ctime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Message insertToDB() {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // get message id.
            String sql = "select COUNT(*) as cnt from message;";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                messageid = rs.getInt("cnt") + 1;
            }
            // insert message into database.
            sql = "insert into message (messageid, content, userid, state) values (?, ?, ?, false)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, messageid);
            ps.setString(2, content);
            ps.setString(3, sender);
            ps.executeUpdate();
            sql = "select ctime from message where messageid=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, messageid);
            rs = ps.executeQuery();
            if (rs.next()) {
                ctime = rs.getString("ctime");
            }
            System.out.println("Message ID: " + messageid + " Create time: " + ctime);
        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public Message updateState(boolean state) {
        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "update message set state=? where messageid=?";
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, state);
            ps.setInt(2, messageid);
            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return String.format("message %d at %s from %s to %s: %s", messageid, ctime, sender, target, content);
    }
}
