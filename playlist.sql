/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  Administrator
 * Created: Mar 6, 2016
 */

DROP TABLE "playlist";

CREATE TABLE "playlist" (
"id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,
"title" VARCHAR NOT NULL DEFAULT "N/A", 
"singer" VARCHAR DEFAULT "N/A", 
"album" VARCHAR DEFAULT "N/A", 
"length" INTEGER NOT NULL ,
 "lyrics" TEXT DEFAULT "N/A", 
"path" VARCHAR NOT NULL  UNIQUE 
);