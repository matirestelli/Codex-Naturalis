package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Card extends CardGame implements Serializable {
    private Map<Integer, Corner> frontCorners;
    private Map<Integer, Corner> backCorners;
    private List<Resource> backResources;
    private Coordinate centre;
    private int xMatrixCord;
    private int yMatrixCord;

    public Map<Integer, Corner> getActualCorners() {
        if (frontSide) return frontCorners;
        else return backCorners;
    }

    public Map<Integer, Corner> getBackCorners() {
        return backCorners;
    }

    public void setBackCorners(Map<Integer, Corner> backCorners) {
        this.backCorners = backCorners;
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
    //                                         idCarte a cui voglio attaccare cardtoplay     id angoli carta a cui posso attaccare  coordinate angoli
    public List<Coordinate> findFreeAngles(int[][] matrix, List<Card> codex, int cardToPlayId, Map<Integer, Map<Integer, List<Coordinate>>> test) {
        AtomicReference<Boolean> pos = new AtomicReference<>(true);
        List<Coordinate> testCorners = new ArrayList<>();
        List<Coordinate> angoliDisponibili = new ArrayList<>();
        Map<Integer, List<Coordinate>> testCornersMap = new HashMap<>();
        int targetId;

        if (this.getActualCorners().containsKey(0) && !this.getActualCorners().get(0).isHidden() && this.getxMatrixCord()-1>=0 && this.getyMatrixCord()+1<=matrix.length-1) {
            if(this.getyMatrixCord() + 2 >= matrix.length) targetId= -1;
            else targetId = matrix[this.getyMatrixCord() + 2][this.getxMatrixCord()];
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

            if(this.getxMatrixCord() - 2 < 0) targetId= -1;
            else targetId = matrix[this.getyMatrixCord()][this.getxMatrixCord() - 2];
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

            if(this.getyMatrixCord() + 2 >= matrix.length || this.getxMatrixCord() - 2 < 0) targetId= -1;
            else targetId = matrix[this.getyMatrixCord() + 2][this.getxMatrixCord() - 2];
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
                angoliDisponibili.add(new Coordinate(this.getId(), 0));
            }
        }

        pos.set(true);
        testCorners.clear();

        if (this.getActualCorners().containsKey(1) && !this.getActualCorners().get(1).isHidden() && this.getxMatrixCord()-1>=0 && this.getyMatrixCord()-1>=0) {
            if (this.getyMatrixCord() - 2 < 0) targetId = -1;
            else targetId = matrix[this.getyMatrixCord() - 2][this.getxMatrixCord()];
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

            if (this.getxMatrixCord() - 2 < 0) targetId = -1;
            else targetId = matrix[this.getyMatrixCord()][this.getxMatrixCord() - 2];
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

            if (this.getyMatrixCord() - 2 < 0 || this.getxMatrixCord() - 2 < 0) targetId = -1;
            else targetId = matrix[this.getyMatrixCord() - 2][this.getxMatrixCord() - 2];
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
                angoliDisponibili.add(new Coordinate(this.getId(), 1));
            }
        }

        pos.set(true);
        testCorners.clear();

        if (this.getActualCorners().containsKey(2) && !this.getActualCorners().get(2).isHidden() && this.getxMatrixCord()+1<=matrix.length-1 && this.getyMatrixCord()-1>=0) {
            if (this.getyMatrixCord() - 2 < 0) targetId = -1;
            else targetId = matrix[this.getyMatrixCord() - 2][this.getxMatrixCord()];
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

            if (this.getxMatrixCord() + 2 >= matrix[0].length) targetId = -1;
            else targetId = matrix[this.getyMatrixCord()][this.getxMatrixCord() + 2];
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

            if (this.getyMatrixCord() - 2 < 0 || this.getxMatrixCord() + 2 >= matrix[0].length) targetId = -1;
            else targetId = matrix[this.getyMatrixCord() - 2][this.getxMatrixCord() + 2];
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
                angoliDisponibili.add(new Coordinate(this.getId(), 2));
            }
        }

        pos.set(true);
        testCorners.clear();

        if (this.getActualCorners().containsKey(3) && !this.getActualCorners().get(3).isHidden() && this.getxMatrixCord()+1<=matrix.length-1 && this.getyMatrixCord()+1<=matrix.length-1) {
            if (this.getyMatrixCord() + 2 >= matrix.length) targetId = -1;
            else targetId = matrix[this.getyMatrixCord() + 2][this.getxMatrixCord()];
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

            if (this.getxMatrixCord() + 2 >= matrix.length) targetId = -1;
            else targetId = matrix[this.getyMatrixCord()][this.getxMatrixCord() + 2];
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

            if (this.getyMatrixCord() + 2 >= matrix[0].length || this.getxMatrixCord() + 2 >= matrix[0].length) targetId = -1;
            else targetId = matrix[this.getyMatrixCord() + 2][this.getxMatrixCord() + 2];
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
                angoliDisponibili.add(new Coordinate(this.getId(), 3));
            }
        }

        test.put(this.getId(), testCornersMap);

        return angoliDisponibili;
    }
}
