/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csci3280;

import java.net.URL;
import java.io.*;
import java.sql.*;

import db.*;
import javafx.application.Platform;
import javafx.event.*;

import java.util.*;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javafx.scene.input.MouseEvent;

/**
 * @author stephycat
 */
public class HomeController implements Initializable {

    @FXML
    private Slider sliderVideoProgress;
    @FXML
    private Button btnStop;
    @FXML
    private ImageView ivVideoImage;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblAlbum;
    @FXML
    private Label lblSinger;
    @FXML
    private Label lblLength;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<PlayList> tvPlayList;
    @FXML
    private TableColumn<PlayList, String> colTitle;
    @FXML
    private TableColumn<PlayList, String> colAlbum;
    @FXML
    private TableColumn<PlayList, String> colSinger;
    @FXML
    private TableColumn<PlayList, String> colLength;
    @FXML
    private ToggleButton btnPlayPause;
    private String statusPlayPause[] = {"Play", "Pause"};
    private int indexPlayPause = 0;

    private SQLite playListDB;
    private Thread avThread;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        colTitle.setCellValueFactory(new PropertyValueFactory<PlayList, String>("title"));
        colAlbum.setCellValueFactory(new PropertyValueFactory<PlayList, String>("album"));
        colSinger.setCellValueFactory(new PropertyValueFactory<PlayList, String>("singer"));
        colLength.setCellValueFactory(new PropertyValueFactory<PlayList, String>("length"));
        /*test*/
        playListDB = new SQLite("3280.sqlite");
        Set<String> titleSet = new HashSet<String>();
//        this.tvPlayList = new TableView<PlayList>();
        this.tvPlayList.setEditable(true);

        try {
            this.loadPlayList(playListDB.displayAll());
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        this.tvPlayList.setVisible(true);
        this.tvPlayList.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                PlayList curPlayList;
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
                    curPlayList = tvPlayList.getSelectionModel().getSelectedItem();
                    lblTitle.setText(curPlayList.getTitle());
                    lblAlbum.setText(curPlayList.getAlbum());
                    lblSinger.setText(curPlayList.getSinger());
                    lblLength.setText(curPlayList.getLength());
                }
            }
        });
    }

    private void loadPlayList(ResultSet rsPlayList) throws SQLException {
        ObservableList olPlayList = FXCollections.observableArrayList();
        this.tvPlayList.getItems().clear();
        while (rsPlayList.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            String title = rsPlayList.getString("title");
            String album = rsPlayList.getString("album");
            String singer = rsPlayList.getString("singer");
            int intLength = rsPlayList.getInt("length");
            PlayList pl = new PlayList(title, album, singer, intLength);
            olPlayList.add(pl);
        }
        this.tvPlayList.setItems(olPlayList);
    }

    @FXML
    private void performSearch(ActionEvent event) {
        System.out.println("Fuck");
        try {
            this.loadPlayList(this.playListDB.search(this.txtSearch.getText()));
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    @FXML
    private void togglePlayPause(ActionEvent event) {
        this.indexPlayPause = (this.indexPlayPause + 1) % 2;
        this.btnPlayPause.setText(this.statusPlayPause[this.indexPlayPause]);

        if (!AudioAndVideoPlayer.shouldPause() && !AudioAndVideoPlayer.isPlaying()) {

            final HomeController homeController = this;

            //TODO:Temp hack to choose video to play
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open the file you would like to play");
            File file = fileChooser.showOpenDialog(Csci3280.getMainStage());

            //AudioAndVideoPlayer.stopPlaying(false);
            //play video and audio with xuggler in a new thread
            Runnable playAudioAndVideo = new Runnable() {
                @Override
                public void run() {
                    try {
                        AudioAndVideoPlayer.playFile(file.getAbsolutePath(), ivVideoImage,sliderVideoProgress,homeController);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            avThread = new Thread(playAudioAndVideo);
            avThread.start();

          /*  Thread infoThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        GetContainerInfo.getInfo(file.getAbsolutePath());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });*/



           /* Runnable playVideo = new Runnable() {
                @Override
                public void run() {
                    try {
                        VideoPlayer.playVideo(file.getAbsolutePath(), homeController);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread videoThread=new Thread(playVideo);

            Runnable playAudio=new Runnable() {
                @Override
                public void run() {
                    try {
                        AudioPlayer.playSound(file.getAbsolutePath());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread audioThread=new Thread(playAudio);

            videoThread.start();
            audioThread.start();
*/


        } else if (AudioAndVideoPlayer.isPlaying() && !AudioAndVideoPlayer.shouldStop()&&!AudioAndVideoPlayer.shouldPause()) {
            AudioAndVideoPlayer.pause(true);

        }else if (AudioAndVideoPlayer.shouldPause() && AudioAndVideoPlayer.isPlaying()) {
            AudioAndVideoPlayer.pause(false);
        }

    }

    public void resetButtonState(){
        btnPlayPause.setSelected(false);
        this.indexPlayPause = (this.indexPlayPause + 1) % 2;
        this.btnPlayPause.setText(this.statusPlayPause[this.indexPlayPause]);
    }

    @FXML
    private void abc(Event event) {
        System.out.println("ff");
    }

    public void onStopBtnClicked(ActionEvent actionEvent) {

        Platform.runLater(new Runnable() {
            @Override public void run() {
                sliderVideoProgress.setValue(sliderVideoProgress.getMin());
            }
        });
        if (AudioAndVideoPlayer.isPlaying()) {
            AudioAndVideoPlayer.stopPlaying(true);

            if (!AudioAndVideoPlayer.shouldPause()) {
                btnPlayPause.setSelected(false);
                this.indexPlayPause = (this.indexPlayPause + 1) % 2;
                this.btnPlayPause.setText(this.statusPlayPause[this.indexPlayPause]);
            }
        }
    }
}
