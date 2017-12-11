package rpc;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;


import org.json.JSONArray;


import algorithm.Recommendation;

import entity.Item;
import org.json.JSONObject;


/**
 * Servlet implementation class RecommendItem
 */

@WebServlet("/recommendation")

public class RecommendItem extends HttpServlet {

    private static final long serialVersionUID = 1L;


    /**
     * @see HttpServlet#HttpServlet()
     */

    public RecommendItem() {

        super();

        // TODO Auto-generated constructor stub

    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response)

            throws ServletException, IOException {

        String userId = request.getParameter("user_id");

        double lat = Double.parseDouble(request.getParameter("lat"));

        double lon = Double.parseDouble(request.getParameter("lon"));

        Recommendation recommendation = new Recommendation();

        List<Item> items = recommendation.recommendItems(userId, lat, lon);


        /**-------------------------------- */
        /**format response data  */
        List<JSONObject> list = new ArrayList<>();
        try {
            for (Item item : items) {
                // Add a thin version of item object
                JSONObject obj = item.toJSONObject();

                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray array = new JSONArray(list);


        // return the format response data
        helper.writeJsonArray(response, array);
        /**-------------------------------- */


    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response)

            throws ServletException, IOException {

        // TODO Auto-generated method stub

        doGet(request, response);

    }

}