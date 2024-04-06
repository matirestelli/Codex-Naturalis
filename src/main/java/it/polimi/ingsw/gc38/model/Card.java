package it.polimi.ingsw.gc38.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Card extends CardGame {
    // private int id;
    // private boolean frontSide;
    // private String frontCover;
    // private String backCover;
    private Map<Integer, Corner> frontCorners;
    private Map<Integer, Corner> backCorners;
    private List<Resource> backResources;
    private Coordinate centre;
    private int xMatrixCord;
    private int yMatrixCord;
    // private Color color;

    public Map<Integer, Corner> getActualCorners() {
        if (isFrontSide()) return frontCorners;
        else return backCorners;
    }

    public Map<Integer, Corner> getBackCorners() {
        return backCorners;
    }

    public void setXYCord(int yMatrixCord, int xMatrixCord) {
        this.xMatrixCord = xMatrixCord;
        this.yMatrixCord = yMatrixCord;
    }

    public int getxMatrixCord() {
        return xMatrixCord;
    }

    public void setxMatrixCord(int xMatrixCord) {
        this.xMatrixCord = xMatrixCord;
    }

    public int getyMatrixCord() {
        return yMatrixCord;
    }

    public void setyMatrixCord(int yMatrixCord) {
        this.yMatrixCord = yMatrixCord;
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public boolean isFrontSide() {
//        return frontSide;
//    }
//
//    public void setSide(boolean frontSide) {
//        this.frontSide = frontSide;
//    }
//
//    public String getFrontCover() {
//        return frontCover;
//    }
//
//    public void setFrontCover(String frontCover) {
//        this.frontCover = frontCover;
//    }
//
//    public String getBackCover() {
//        return backCover;
//    }
//
//    public void setBackCover(String backCover) {
//        this.backCover = backCover;
//    }

    public Map<Integer, Corner> getFrontCorners() {
        return frontCorners;
    }

    public void setFrontCorners(Map<Integer, Corner> frontCorners) {
        this.frontCorners = frontCorners;
    }

    public List<Resource> getBackResources() {
        return backResources;
    }

    public void setBackResources(List<Resource> backResources) {
        this.backResources = backResources;
    }

    public Coordinate getCentre() {
        return centre;
    }

    public void setCentre(Coordinate centre) {
        this.centre = centre;
    }

    public List<Coordinate> trovaCarteVicine(int[][] matrix, List<Card> codex, int cardToPlayId, Map<Integer, Map<Integer, List<Coordinate>>> test) {
        AtomicReference<Boolean> pos = new AtomicReference<>(true);
        List<Coordinate> testCorners = new ArrayList<>();
        List<Coordinate> angoliDispobibili = new ArrayList<>();
        Map<Integer, List<Coordinate>> testCornersMap = new HashMap<>();
        int targetId;

        if (this.getActualCorners().containsKey(0) && !this.getActualCorners().get(0).isHidden()) {
            targetId = matrix[this.getyMatrixCord() + 2][this.getxMatrixCord()];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(1)) {
                                // new angle of the card to play covered
                                testCorners.add(new Coordinate(cardToPlayId, 3));
                                // angle covered by the card to play
                                testCorners.add(new Coordinate(finalTargetId, 1));
                            } else
                                pos.set(false);
                        });
            }

            targetId = matrix[this.getyMatrixCord()][this.getxMatrixCord() - 2];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(3)) {
                                // new angle of the card to play covered
                                testCorners.add(new Coordinate(cardToPlayId, 1));
                                // angle covered by the card to play
                                testCorners.add(new Coordinate(finalTargetId, 3));
                            } else
                                pos.set(false);
                        });
            }

            targetId = matrix[this.getyMatrixCord() + 2][this.getxMatrixCord() - 2];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(2)) {
                                // new angle of the card to play covered
                                testCorners.add(new Coordinate(cardToPlayId, 0));
                                // angle covered by the card to play
                                testCorners.add(new Coordinate(finalTargetId, 2));
                            } else
                                pos.set(false);
                        });
            }

            testCorners.add(new Coordinate(this.getId(), 0));
            testCorners.add(new Coordinate(cardToPlayId, 2));

            if (pos.get()) {
                testCornersMap.put(0, new ArrayList<>(testCorners));
                angoliDispobibili.add(new Coordinate(this.getId(), 0));
            }
        }

        pos.set(true);
        testCorners.clear();

        if (this.getActualCorners().containsKey(1) && !this.getActualCorners().get(1).isHidden()) {
            targetId = matrix[this.getyMatrixCord() - 2][this.getxMatrixCord()];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(0)) {
                                testCorners.add(new Coordinate(cardToPlayId, 2));
                                testCorners.add(new Coordinate(finalTargetId, 0));
                            } else
                                pos.set(false);
                        });
            }

            targetId = matrix[this.getyMatrixCord()][this.getxMatrixCord() - 2];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(2)) {
                                testCorners.add(new Coordinate(cardToPlayId, 0));
                                testCorners.add(new Coordinate(finalTargetId, 2));
                            } else
                                pos.set(false);
                        });
            }

            targetId = matrix[this.getyMatrixCord() - 2][this.getxMatrixCord() - 2];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(3)) {
                                testCorners.add(new Coordinate(cardToPlayId, 1));
                                testCorners.add(new Coordinate(finalTargetId, 3));
                            } else
                                pos.set(false);
                        });
            }

            testCorners.add(new Coordinate(this.getId(), 1));
            testCorners.add(new Coordinate(cardToPlayId, 3));

            if (pos.get()) {
                testCornersMap.put(1, new ArrayList<>(testCorners));
                angoliDispobibili.add(new Coordinate(this.getId(), 1));
            }
        }

        pos.set(true);
        testCorners.clear();

        if (this.getActualCorners().containsKey(2) && !this.getActualCorners().get(2).isHidden()) {
            targetId = matrix[this.getyMatrixCord() - 2][this.getxMatrixCord()];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(3)) {
                                testCorners.add(new Coordinate(cardToPlayId, 1));
                                testCorners.add(new Coordinate(finalTargetId, 3));
                            } else
                                pos.set(false);
                        });
            }

            targetId = matrix[this.getyMatrixCord()][this.getxMatrixCord() + 2];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(1)) {
                                testCorners.add(new Coordinate(cardToPlayId, 3));
                                testCorners.add(new Coordinate(finalTargetId, 1));
                            } else
                                pos.set(false);
                        });
            }

            targetId = matrix[this.getyMatrixCord() - 2][this.getxMatrixCord() + 2];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(0)) {
                                testCorners.add(new Coordinate(cardToPlayId, 2));
                                testCorners.add(new Coordinate(finalTargetId, 0));
                            } else
                                pos.set(false);
                        });
            }

            testCorners.add(new Coordinate(this.getId(), 2));
            testCorners.add(new Coordinate(cardToPlayId, 0));

            if (pos.get()) {
                testCornersMap.put(2, new ArrayList<>(testCorners));
                angoliDispobibili.add(new Coordinate(this.getId(), 2));
            }
        }

        pos.set(true);
        testCorners.clear();

        if (this.getActualCorners().containsKey(3) && !this.getActualCorners().get(3).isHidden()) {
            targetId = matrix[this.getyMatrixCord() + 2][this.getxMatrixCord()];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(2)) {
                                testCorners.add(new Coordinate(cardToPlayId, 0));
                                testCorners.add(new Coordinate(finalTargetId, 2));
                            } else
                                pos.set(false);
                        });
            }

            targetId = matrix[this.getyMatrixCord()][this.getxMatrixCord() + 2];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(0)) {
                                testCorners.add(new Coordinate(cardToPlayId, 2));
                                testCorners.add(new Coordinate(finalTargetId, 0));
                            } else
                                pos.set(false);
                        });
            }

            targetId = matrix[this.getyMatrixCord() + 2][this.getxMatrixCord() + 2];
            if (targetId != -1) {
                int finalTargetId = targetId;
                codex.stream()
                        .filter(card -> card.getId() == finalTargetId)
                        .findAny()
                        .ifPresent(card -> {
                            if (card.getActualCorners().containsKey(1)) {
                                testCorners.add(new Coordinate(cardToPlayId, 3));
                                testCorners.add(new Coordinate(finalTargetId, 1));
                            } else
                                pos.set(false);
                        });
            }

            testCorners.add(new Coordinate(this.getId(), 3));
            testCorners.add(new Coordinate(cardToPlayId, 1));

            if (pos.get()) {
                testCornersMap.put(3, new ArrayList<>(testCorners));
                angoliDispobibili.add(new Coordinate(this.getId(), 3));
            }
        }

        test.put(this.getId(), testCornersMap);

        return angoliDispobibili;
    }

//    public void setColor(Color color) {
//        this.color = color;
//    }
//
//    public Color getColor() {
//        return color;
//    }
}
