package net.anei.cadpage.parsers.NH;

import java.util.Properties;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Grafton County, NH
*/


public class NHGraftonCountyParser extends GroupBestParser {
  
  public NHGraftonCountyParser() {
    super(new NHGraftonCountyAParser(), new NHGraftonCountyBParser());
  }
}
