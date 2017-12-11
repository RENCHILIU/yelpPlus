package rpc;

import db.mysql.MySQLConnection;
import entity.Item;
import external.yelpApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SearchItem extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MySQLConnection conn = MySQLConnection.getInstance();





    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {



        /** this will be replaced by the  MySQLConnection
         * insert this code in MySQLConnection.
         * so when we connect yelpAPI, we can save the data to the database
         *
         *
        -----  original code --------
         yelpApi api = new yelpApi();
         List<Item> items = api.search(lat, lon, term);
        */

        String userId = request.getParameter("user_id");
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));
        // Term can be empty or null.
        String term = request.getParameter("term");



        /**-------------------------------- */
        /** search the yelp API */
        /** write to the DataBase */
        List<Item> items = conn.searchItems(userId, lat, lon, term);
        /**-------------------------------- */




        /**-------------------------------- */
        /**format response data  */
        List<JSONObject> list = new ArrayList<>();

        /** in the search result get the favorite items already marked by user.*/
        Set<String> favorite = conn.getFavoriteItemIds(userId);

        try {
            for (Item item : items) {
                // Add a thin version of item object
                JSONObject obj = item.toJSONObject();

                /**-------------------------------- */
                /** Check if this is a favorite one.*/
                /** This field is required by frontend to correctly display favorite items.*/

                obj.put("favorite", favorite.contains(item.getItemId()));

                /**-------------------------------- */


                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ouput format
        JSONArray array = new JSONArray(list);
        helper.writeJsonArray(response, array);
        /**-------------------------------- */


    }


}
