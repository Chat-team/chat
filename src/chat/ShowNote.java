package chat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Dongfang on 2015/12/26.
 */
@WebServlet(name = "ShowNote", urlPatterns = {"/shownote"})
public class ShowNote extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ReqReader reader = new ReqReader(request.getInputStream());
        ResWriter writer = new ResWriter(response.getOutputStream());

        String username, boardid;
        HttpSession session = request.getSession();
        if (session.getAttribute("userid") != null) {
            username = (String)session.getAttribute("userid");
        }
        else {
            response.setHeader("Location", "/");
            response.setStatus(401);
            return; // no valid userid.
        }
        boardid = reader.getString("boardid");

        if (username == "" || boardid == "" ) {
            writer.add("status", "failed").write();
            return;
        }

        DatabaseConnection dbConn = new DatabaseConnection();
        Connection conn = dbConn.getConnection();
        String sql = "SELECT ctime, content, userid FROM note_belong, note WHERE note_belong.noteid = note.noteid AND boardid = ?";
        PreparedStatement ps = null;
        ResultSet rs = null, rt;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, boardid);
            rs = ps.executeQuery();
            ArrayList<Map<String, String>> array = new ArrayList<>();
            while (rs.next()) {
                Map<String, String> m = new TreeMap<>();
                m.put("userid", rs.getString("userid"));
                m.put("ctime", rs.getString("ctime"));
                m.put("content", rs.getString("content"));
                sql = "select nickname from user_info where userid = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, rs.getString("userid"));
                rt = ps.executeQuery();
                if (rt.next()) {
                    m.put("nickname", rt.getString("nickname"));
                }
                array.add(m);
            }
            writer.add("status", "success");
            writer.add("note", array).write();
        } catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
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
    }

}
