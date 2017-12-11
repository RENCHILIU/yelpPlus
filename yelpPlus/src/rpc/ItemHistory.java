package rpc;

import db.mysql.MySQLConnection;
import entity.Item;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(name = "rpc.ItemHistory")
public class ItemHistory extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MySQLConnection conn = MySQLConnection.getInstance();








    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject input = helper.readJsonObject(request);
            String userId = input.getString("user_id");
            JSONArray array = (JSONArray) input.get("favorite");

            List<String> histories = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                String itemId = (String) array.get(i);
                histories.add(itemId);
            }

            /** write to the DataBase */
            conn.setFavoriteItems(userId, histories);

            helper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /** delete the favorite item seted in the doPost*/
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            JSONObject input = helper.readJsonObject(req);
            String userId = input.getString("user_id");
            JSONArray array = (JSONArray) input.get("favorite");

            List<String> histories = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                String itemId = (String) array.get(i);
                histories.add(itemId);
            }
            conn.unsetFavoriteItems(userId, histories);
            helper.writeJsonObject(resp, new JSONObject().put("result", "SUCCESS"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userId = request.getParameter("user_id");
        Set<Item> items = conn.getFavoriteItems(userId);
        JSONArray array = new JSONArray();
        for (Item item : items) {
            JSONObject obj = item.toJSONObject();

            /** add favorite mark in the return result  */
            try {
                obj.append("favorite", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            array.put(obj);
        }
        helper.writeJsonArray(response, array);



    }
}
