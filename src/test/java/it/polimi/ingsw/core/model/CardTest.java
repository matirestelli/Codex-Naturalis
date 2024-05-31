package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;
    private Card card5;
    private Card card6;
    private Card card7;
    private Card card8;
    private Card card9;
    private Card cardToplay;
    private PlayerState player;
    private Map<Integer, Corner> frontCorners1 = new HashMap<>();
    private Map<Integer, Corner> backCorners1 = new HashMap<>();

    @BeforeEach
    void setUp() {
        List<Card> codex = new ArrayList<>();
        player = new PlayerState();
        player.setCodex(codex);
        card1 = new ResourceCard();
        card1.setId(1);
        card1.setSide(true);
        card1.setFrontCover("frontCover");
        card1.setBackCover("backCover");
        card1.setBackResources(new ArrayList<>());
        card1.setBackCorners(new HashMap<>());

        frontCorners1.put(0, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners1.put(1, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners1.put(2, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners1.put(3, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        backCorners1.put(0, new Corner() {{
            setEmpty(false);
            setResource(Resource.ANIMAL);
        }});
        card1.setBackCorners(backCorners1);
        card1.setFrontCorners(frontCorners1);
        card1.setCentre(new Coordinate(1, 1));
        card1.setxMatrixCord(1);
        card1.setyMatrixCord(1);
        card1.setColor(Color.BLUE);
        player.addCardToCodex(card1);

        card2 = new ResourceCard();
        card2.setId(2);
        card2.setSide(true);
        card2.setFrontCover("frontCover");
        card2.setBackCover("backCover");
        card2.setFrontCorners(new HashMap<>());
        Map<Integer, Corner> frontCorners2 = new HashMap<>();
        frontCorners2.put(0, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners2.put(1, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners2.put(2, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners2.put(3, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        card2.setFrontCorners(frontCorners2);
        card2.setCentre(new Coordinate(1, 1));
        card2.setxMatrixCord(1);
        card2.setyMatrixCord(1);
        card2.setColor(Color.BLUE);
        player.addCardToCodex(card2);

        card3 = new ResourceCard();
        card3.setId(3);
        card3.setSide(true);
        card3.setFrontCover("frontCover");
        card3.setBackCover("backCover");
        card3.setFrontCorners(new HashMap<>());
        Map<Integer, Corner> frontCorners3 = new HashMap<>();
        frontCorners3.put(0, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners3.put(1, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners3.put(2, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners3.put(3, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        card3.setFrontCorners(frontCorners3);
        card3.setCentre(new Coordinate(1, 1));
        card3.setxMatrixCord(0);
        card3.setyMatrixCord(2);
        card3.setColor(Color.BLUE);
        player.addCardToCodex(card3);

        card4 = new ResourceCard();
        card4.setId(4);
        card4.setSide(true);
        card4.setFrontCover("frontCover");
        card4.setBackCover("backCover");
        card4.setFrontCorners(new HashMap<>());
        Map<Integer, Corner> frontCorners4 = new HashMap<>();
        frontCorners4.put(0, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners4.put(1, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners4.put(2, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners4.put(3, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        card4.setFrontCorners(frontCorners4);
        card4.setCentre(new Coordinate(1, 1));
        card4.setxMatrixCord(1);
        card4.setyMatrixCord(3);
        card4.setColor(Color.BLUE);
        player.addCardToCodex(card4);

        card5 = new ResourceCard();
        card5.setId(5);
        card5.setSide(true);
        card5.setFrontCover("frontCover");
        card5.setBackCover("backCover");
        card5.setFrontCorners(new HashMap<>());
        Map<Integer, Corner> frontCorners5 = new HashMap<>();
        frontCorners5.put(0, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners5.put(1, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners5.put(2, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners5.put(3, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        card5.setFrontCorners(frontCorners5);
        card5.setCentre(new Coordinate(1, 1));
        card5.setxMatrixCord(0);
        card5.setyMatrixCord(4);
        card5.setColor(Color.BLUE);
        player.addCardToCodex(card5);

        card6 = new ResourceCard();
        card6.setId(6);
        card6.setSide(true);
        card6.setFrontCover("frontCover");
        card6.setBackCover("backCover");
        card6.setFrontCorners(new HashMap<>());
        Map<Integer, Corner> frontCorners6 = new HashMap<>();
        frontCorners6.put(0, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners6.put(1, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners6.put(2, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners6.put(3, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        card6.setFrontCorners(frontCorners6);
        card6.setCentre(new Coordinate(1, 1));
        card6.setxMatrixCord(0);
        card6.setyMatrixCord(4);
        card6.setColor(Color.BLUE);
        player.addCardToCodex(card6);

        card7 = new ResourceCard();
        card7.setId(7);
        card7.setSide(true);
        card7.setFrontCover("frontCover");
        card7.setBackCover("backCover");
        card7.setFrontCorners(new HashMap<>());
        Map<Integer, Corner> frontCorners7 = new HashMap<>();
        frontCorners7.put(0, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners7.put(1, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners7.put(2, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners7.put(3, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        card7.setFrontCorners(frontCorners7);
        card7.setCentre(new Coordinate(1, 1));
        card7.setxMatrixCord(0);
        card7.setyMatrixCord(4);
        card7.setColor(Color.BLUE);
        player.addCardToCodex(card7);

        card8 = new ResourceCard();
        card8.setId(8);
        card8.setSide(true);
        card8.setFrontCover("frontCover");
        card8.setBackCover("backCover");
        card8.setFrontCorners(new HashMap<>());
        Map<Integer, Corner> frontCorners8 = new HashMap<>();
        frontCorners8.put(0, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners8.put(1, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners8.put(2, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners8.put(3, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        card8.setFrontCorners(frontCorners8);
        card8.setCentre(new Coordinate(1, 1));
        card8.setxMatrixCord(0);
        card8.setyMatrixCord(4);
        card8.setColor(Color.BLUE);
        player.addCardToCodex(card8);

        card9 = new ResourceCard();
        card9.setId(9);
        card9.setSide(true);
        card9.setFrontCover("frontCover");
        card9.setBackCover("backCover");
        card9.setFrontCorners(new HashMap<>());
        Map<Integer, Corner> frontCorners9 = new HashMap<>();
        frontCorners9.put(0, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners9.put(1, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners9.put(2, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        frontCorners9.put(3, new Corner() {{
            setEmpty(false);
            setResource(Resource.PLANT);
        }});
        card9.setFrontCorners(frontCorners9);
        card9.setCentre(new Coordinate(1, 1));
        card9.setxMatrixCord(0);
        card9.setyMatrixCord(4);
        card9.setColor(Color.BLUE);
        player.addCardToCodex(card9);
    }


    @Test
    void testgetId() {
        assertEquals(1, card1.getId());
    }

    @Test
    void testsetId() {
        card1.setId(2);
        assertEquals(2, card1.getId());
    }

    @Test
    void testisFrontSide() {
        card1.setSide(true);
        assertTrue(card1.isFrontSide());
        card1.setSide(false);
        assertFalse(card1.isFrontSide());
    }

    @Test
    void testsetSide() {
        card1.setSide(false);
        assertFalse(card1.isFrontSide());
    }

    @Test
    void testgetFrontCover() {
        assertEquals("frontCover", card1.getFrontCover());
    }

    @Test
    void testsetFrontCover() {
        card1.setFrontCover("newFrontCover");
        assertEquals("newFrontCover", card1.getFrontCover());
    }

    @Test
    void testgetBackCover() {
        assertEquals("backCover", card1.getBackCover());
    }

    @Test
    void testsetBackCover() {
        card1.setBackCover("newBackCover");
        assertEquals("newBackCover", card1.getBackCover());
    }

    @Test
    void testgetFrontCorners() {
        assertNotNull(card1.getFrontCorners());
    }

    @Test
    void testgetBackResources() {
        assertNotNull(card1.getBackResources());
    }

    @Test
    void testgetCentre() {
        assertNotNull(card1.getCentre());
    }

    @Test
    void testsetCentre() {
        card1.setCentre(new Coordinate(2, 2));
        assertEquals(new Coordinate(2, 2), card1.getCentre());
    }

    @Test
    void testgetxMatrixCord() {
        assertEquals(1, card1.getxMatrixCord());
    }

    @Test
    void testsetxMatrixCord() {
        card1.setxMatrixCord(2);
        assertEquals(2, card1.getxMatrixCord());
    }

    @Test
    void testgetyMatrixCord() {
        assertEquals(1, card1.getyMatrixCord());
    }

    @Test
    void testsetyMatrixCord() {
        card1.setyMatrixCord(2);
        assertEquals(2, card1.getyMatrixCord());
    }

    @Test
    void testgetColor() {
        assertEquals(Color.BLUE, card1.getColor());
    }

    @Test
    void testsetColor() {
        card1.setColor(Color.GREEN);
        assertEquals(Color.GREEN, card1.getColor());
    }

    @Test
    void testsetXYCord() {
        card1.setXYCord(2, 2);
        assertEquals(2, card1.getxMatrixCord());
        assertEquals(2, card1.getyMatrixCord());
    }

    @Test
    void testgetcolor() {
        assertEquals(Color.BLUE, card1.getColor());
    }

    @Test
    void testsetcolor() {
        card1.setColor(Color.GREEN);
        assertEquals(Color.GREEN, card1.getColor());
    }

    @Test
    void testgetactualcorners() {
        assertNotNull(card1.getActualCorners());
        assertEquals(card1.getActualCorners(), frontCorners1);
        card1.setSide(false);
        assertEquals(card1.getActualCorners(), backCorners1);
    }

    @Test
    void testgetbackcorners() {
        assertNotNull(card1.getBackCorners());
    }


    @Test
    void testsetbackcorners() {
        Map<Integer, Corner> backCorners = new HashMap<>();
        backCorners.put(1, new Corner());
        card1.setBackCorners(backCorners);
        assertEquals(backCorners, card1.getBackCorners());
    }


    @Test
    void testsetFrontCorners() {
        Map<Integer, Corner> frontCorners = new HashMap<>();
        frontCorners.put(1, new Corner());
        card1.setFrontCorners(frontCorners);
        assertEquals(frontCorners, card1.getFrontCorners());
    }


    @Test
    void testsetBackResources() {
        List<Resource> backResources = new ArrayList<>();
        backResources.add(Resource.ANIMAL);
        card1.setBackResources(backResources);
        assertEquals(backResources, card1.getBackResources());
    }


    @Test
    void testfindFreeAngles(){
        card1.getActualCorners().get(0).setHidden(true);
        player.setMatrix(new int[][] {
                { -1, -1, -1, -1, -1, -1, -1},
                { -1,  1, -1, -1, -1, -1, -1},
                {  3, -1, -1, -1, -1, -1, -1},
                { -1,  4, -1, -1, -1, -1, -1},
                {  5, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        angoliDisponibili.add(new Coordinate(1, 1));
        angoliDisponibili.add(new Coordinate(1, 2));
        angoliDisponibili.add(new Coordinate(1, 3));
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));
    }

    @Test
    void testfindFreeAngles2(){
        card1.setxMatrixCord(3);
        card1.setyMatrixCord(4);
        card2.setxMatrixCord(4);
        card2.setyMatrixCord(3);
        card3.setxMatrixCord(2);
        card3.setyMatrixCord(5);
        card4.setxMatrixCord(4);
        card4.setyMatrixCord(5);
        card5.setxMatrixCord(2);
        card5.setyMatrixCord(4);
        card1.getActualCorners().get(0).setHidden(true);
        card1.getActualCorners().get(1).setHidden(true);
        card1.getActualCorners().get(2).setHidden(true);
        card1.getActualCorners().get(3).setHidden(true);


        player.setMatrix(new int[][] {
                            // 3
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1,  5, -1,  2, -1, -1},
                { -1, -1, -1,  1, -1, -1, -1}, // 4
                { -1, -1,  3, -1,  4, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));
    }

    @Test
    void testfindFreeAngles3(){
        card1.setxMatrixCord(3);
        card1.setyMatrixCord(4);
        card2.setxMatrixCord(5);
        card2.setyMatrixCord(2);
        card3.setxMatrixCord(5);
        card3.setyMatrixCord(4);
        card4.setxMatrixCord(5);
        card4.setyMatrixCord(6);
        card5.setxMatrixCord(3);
        card5.setyMatrixCord(6);
        card6.setxMatrixCord(1);
        card6.setyMatrixCord(6);
        card7.setxMatrixCord(1);
        card7.setyMatrixCord(4);
        card8.setxMatrixCord(1);
        card8.setyMatrixCord(2);
        card9.setxMatrixCord(3);
        card9.setyMatrixCord(2);


        player.setMatrix(new int[][] {
                // 3
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1,  8, -1,  9, -1,  2, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1,  7, -1,  1, -1,  3, -1}, // 4
                { -1, -1, -1, -1, -1, -1, -1},
                { -1,  6, -1,  5, -1,  4, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        angoliDisponibili.add(new Coordinate(1, 0));
        angoliDisponibili.add(new Coordinate(1, 1));
        angoliDisponibili.add(new Coordinate(1, 2));
        angoliDisponibili.add(new Coordinate(1, 3));
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));
    }

    @Test
    void testfindFreeAngles4(){
        card1.setxMatrixCord(3);
        card1.setyMatrixCord(4);
        card2.setxMatrixCord(5);
        card2.setyMatrixCord(2);
        card3.setxMatrixCord(5);
        card3.setyMatrixCord(4);
        card4.setxMatrixCord(5);
        card4.setyMatrixCord(6);
        card5.setxMatrixCord(3);
        card5.setyMatrixCord(6);
        card6.setxMatrixCord(1);
        card6.setyMatrixCord(6);
        card7.setxMatrixCord(1);
        card7.setyMatrixCord(4);
        card8.setxMatrixCord(1);
        card8.setyMatrixCord(2);
        card9.setxMatrixCord(3);
        card9.setyMatrixCord(2);

        card5.getFrontCorners().remove(1);
        card7.getFrontCorners().remove(3);
        card6.getFrontCorners().remove(2);
        card9.getFrontCorners().remove(0);
        card7.getFrontCorners().remove(2);
        card8.getFrontCorners().remove(3);
        card9.getFrontCorners().remove(3);
        card2.getFrontCorners().remove(0);
        card3.getFrontCorners().remove(1);
        card3.getFrontCorners().remove(0);
        card4.getFrontCorners().remove(1);
        card5.getFrontCorners().remove(2);
        player.setMatrix(new int[][] {
                            // 3
                { -1, -1, -1, -1, -1, -1, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1,  8, -1,  9, -1,  2, -1},
                { -1, -1, -1, -1, -1, -1, -1},
                { -1,  7, -1,  1, -1,  3, -1}, // 4
                { -1, -1, -1, -1, -1, -1, -1},
                { -1,  6, -1,  5, -1,  4, -1},
                { -1, -1, -1, -1, -1, -1, -1}
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));
    }

    @Test
    void testfindFreeAnglesExitDown(){
        card1.setxMatrixCord(3);
        card1.setyMatrixCord(6);
        card2.setxMatrixCord(5);
        card2.setyMatrixCord(2);
        card3.setxMatrixCord(5);
        card3.setyMatrixCord(4);
        card4.setxMatrixCord(5);
        card4.setyMatrixCord(6);
        card5.setxMatrixCord(3);
        card5.setyMatrixCord(6);
        card6.setxMatrixCord(1);
        card6.setyMatrixCord(6);
        card7.setxMatrixCord(1);
        card7.setyMatrixCord(4);
        card8.setxMatrixCord(1);
        card8.setyMatrixCord(2);
        card9.setxMatrixCord(3);
        card9.setyMatrixCord(2);


        card5.getFrontCorners().remove(1);
        card7.getFrontCorners().remove(3);
        card6.getFrontCorners().remove(2);
        card9.getFrontCorners().remove(0);
        card7.getFrontCorners().remove(2);
        card8.getFrontCorners().remove(3);
        card9.getFrontCorners().remove(3);
        card2.getFrontCorners().remove(0);
        card3.getFrontCorners().remove(1);
        card3.getFrontCorners().remove(0);
        card4.getFrontCorners().remove(1);
        card5.getFrontCorners().remove(2);
        player.setMatrix(new int[][] {
                // 0   1   2   3   4   5   6
                { -1, -1, -1, -1, -1, -1, -1}, // 0
                { -1, -1, -1, -1, -1, -1, -1}, // 1
                { -1,  8, -1,  9, -1,  2, -1}, // 2
                { -1, -1, -1, -1, -1, -1, -1}, // 3
                { -1,  7, -1, -1, -1,  3, -1}, // 4
                { -1, -1, -1, -1, -1, -1, -1}, // 5
                { -1,  6, -1,  1, -1,  4, -1}, // 6
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));
    }

    @Test
    void testfindFreeAnglesExitLeft(){
        card1.setxMatrixCord(0);
        card1.setyMatrixCord(2);

        player.setMatrix(new int[][] {
                // 0   1   2   3   4   5   6
                { -1, -1, -1, -1, -1, -1, -1}, // 0
                { -1, -1, -1, -1, -1, -1, -1}, // 1
                {  1, -1, -1,  9, -1,  2, -1}, // 2
                { -1, -1, -1, -1, -1, -1, -1}, // 3
                { -1,  7, -1, -1, -1,  3, -1}, // 4
                { -1, -1, -1, -1, -1, -1, -1}, // 5
                { -1,  6, -1, -1, -1,  4, -1}, // 6
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        angoliDisponibili.add(new Coordinate(1, 2));
        angoliDisponibili.add(new Coordinate(1, 3));
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));
    }

    @Test
    void testfindFreeAnglesExitNoAngles(){
        card1.setxMatrixCord(0);
        card1.setyMatrixCord(2);
        card2.setxMatrixCord(5);
        card2.setyMatrixCord(2);
        card3.setxMatrixCord(5);
        card3.setyMatrixCord(4);
        card4.setxMatrixCord(5);
        card4.setyMatrixCord(6);
        card5.setxMatrixCord(3);
        card5.setyMatrixCord(6);
        card6.setxMatrixCord(1);
        card6.setyMatrixCord(6);
        card7.setxMatrixCord(1);
        card7.setyMatrixCord(4);
        card8.setxMatrixCord(1);
        card8.setyMatrixCord(2);
        card9.setxMatrixCord(3);
        card9.setyMatrixCord(2);

        card1.getFrontCorners().clear();

        card5.getFrontCorners().remove(1);
        card7.getFrontCorners().remove(3);
        card6.getFrontCorners().remove(2);
        card9.getFrontCorners().remove(0);
        card7.getFrontCorners().remove(2);
        card8.getFrontCorners().remove(3);
        card9.getFrontCorners().remove(3);
        card2.getFrontCorners().remove(0);
        card3.getFrontCorners().remove(1);
        card3.getFrontCorners().remove(0);
        card4.getFrontCorners().remove(1);
        card5.getFrontCorners().remove(2);
        player.setMatrix(new int[][] {
                // 0   1   2   3   4   5   6
                { -1, -1, -1, -1, -1, -1, -1}, // 0
                { -1, -1, -1, -1, -1, -1, -1}, // 1
                {  1, -1, -1,  9, -1,  2, -1}, // 2
                { -1, -1, -1, -1, -1, -1, -1}, // 3
                { -1,  7, -1, -1, -1,  3, -1}, // 4
                { -1, -1, -1, -1, -1, -1, -1}, // 5
                { -1,  6, -1, -1, -1,  4, -1}, // 6
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));
    }

    @Test
    void testfindFreeAnglesExitUp(){
        card1.setxMatrixCord(3);
        card1.setyMatrixCord(0);

        player.setMatrix(new int[][] {
                // 0   1   2   3   4   5   6
                { -1, -1, -1,  1, -1, -1, -1}, // 0
                {  5, -1, -1, -1, -1, -1, -1}, // 1
                { -1,  8, -1,  9, -1,  2, -1}, // 2
                { -1, -1, -1, -1, -1, -1, -1}, // 3
                { -1,  7, -1, -1, -1,  3, -1}, // 4
                { -1, -1, -1, -1, -1, -1, -1}, // 5
                { -1,  6, -1, -1, -1, -1, -1}, // 6
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        angoliDisponibili.add(new Coordinate(1, 0));
        angoliDisponibili.add(new Coordinate(1, 3));
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));
    }

    @Test
    void testfindFreeAnglesExitRight(){
        card1.setxMatrixCord(6);
        card1.setyMatrixCord(3);

        player.setMatrix(new int[][] {
                // 0   1   2   3   4   5   6
                { -1, -1, -1, -1, -1, -1, -1}, // 0
                {  5, -1, -1, -1, -1, -1, -1}, // 1
                { -1,  8, -1,  9, -1,  2, -1}, // 2
                { -1, -1, -1, -1, -1, -1,  1}, // 3
                { -1,  7, -1, -1, -1,  3, -1}, // 4
                { -1, -1, -1, -1, -1, -1, -1}, // 5
                { -1,  6, -1, -1, -1, -1, -1}, // 6
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        angoliDisponibili.add(new Coordinate(1, 0));
        angoliDisponibili.add(new Coordinate(1, 1));
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));
    }

    @Test
    void testfindFreeAnglesExitNearly0(){
        card1.setxMatrixCord(3);
        card1.setyMatrixCord(5);

        player.setMatrix(new int[][] {
                // 0   1   2   3   4   5   6
                { -1, -1, -1, -1, -1, -1, -1}, // 0
                {  5, -1, -1, -1, -1, -1, -1}, // 1
                { -1,  8, -1,  9, -1,  2, -1}, // 2
                { -1, -1, -1, -1, -1, -1, -1}, // 3
                { -1,  7, -1, -1, -1,  3, -1}, // 4
                { -1, -1, -1,  1, -1, -1, -1}, // 5
                { -1,  6, -1, -1, -1, -1, -1}, // 6
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        angoliDisponibili.add(new Coordinate(1, 0));
        angoliDisponibili.add(new Coordinate(1, 1));
        angoliDisponibili.add(new Coordinate(1, 2));
        angoliDisponibili.add(new Coordinate(1, 3));
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));

    }

    @Test
    void testfindFreeAnglesExitNearly1(){
        card1.setxMatrixCord(1);
        card1.setyMatrixCord(3);

        player.setMatrix(new int[][] {
                // 0   1   2   3   4   5   6
                { -1, -1, -1, -1, -1, -1, -1}, // 0
                {  5, -1, -1, -1, -1, -1, -1}, // 1
                { -1,  8, -1,  9, -1,  2, -1}, // 2
                { -1,  1, -1, -1, -1, -1, -1}, // 3
                { -1,  7, -1, -1, -1,  3, -1}, // 4
                { -1, -1, -1, -1, -1, -1, -1}, // 5
                { -1,  6, -1, -1, -1, -1, -1}, // 6
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        angoliDisponibili.add(new Coordinate(1, 0));
        angoliDisponibili.add(new Coordinate(1, 1));
        angoliDisponibili.add(new Coordinate(1, 2));
        angoliDisponibili.add(new Coordinate(1, 3));
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));

    }

    @Test
    void testfindFreeAnglesExitNearly2(){
        card1.setxMatrixCord(5);
        card1.setyMatrixCord(2);

        player.setMatrix(new int[][] {
                // 0   1   2   3   4   5   6
                { -1, -1, -1, -1, -1, -1, -1}, // 0
                {  5, -1, -1, -1, -1, -1, -1}, // 1
                { -1,  8, -1,  9, -1,  1, -1}, // 2
                { -1, -1, -1, -1, -1, -1, -1}, // 3
                { -1,  7, -1, -1, -1,  3, -1}, // 4
                { -1, -1, -1, -1, -1, -1, -1}, // 5
                { -1,  6, -1, -1, -1, -1, -1}, // 6
        });

        List<Coordinate> angoliDisponibili = new ArrayList<>();
        angoliDisponibili.add(new Coordinate(1, 0));
        angoliDisponibili.add(new Coordinate(1, 1));
        angoliDisponibili.add(new Coordinate(1, 2));
        angoliDisponibili.add(new Coordinate(1, 3));
        Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

        assertEquals(angoliDisponibili, card1.findFreeAngles(player.getMatrix(), player.getCodex(), 8, test));

    }











}