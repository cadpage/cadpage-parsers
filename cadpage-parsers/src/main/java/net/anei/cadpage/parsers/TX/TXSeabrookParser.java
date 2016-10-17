package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA18Parser;


public class TXSeabrookParser extends DispatchA18Parser {
  
  private static final Pattern SUBJECT_SRC_PTN = Pattern.compile("[A-Z]{1,3}FD");
  
  public TXSeabrookParser() {
    super(CITY_LIST, "HARRIS COUNTY","TX");
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
 
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,jvfire@ci.jersey-village.tx.us";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (SUBJECT_SRC_PTN.matcher(subject).matches()) data.strSource = subject;
    return super.parseMsg(body, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("Northwest Freeway SR", "Northwest Freeway");
  }
  
  
  private static String[] CITY_LIST = new String[]{

    // Cities    
    "BAYTOWN",
    "BELLAIRE",
    "BUNKER HILL VILLAGE",
    "DEER PARK",
    "EL LAGO",
    "FRIENDSWOOD",
    "GALENA PARK",
    "HEDWIG VILLAGE",
    "HILSHIRE VILLAGE",
    "HOUSTON",
    "HUMBLE",
    "HUNTERS CREEK VILLAGE",
    "JACINTO CITY",
    "JERSEY VILLAGE",
    "KATY",
    "LA PORTE",
    "LEAGUE CITY",
    "MISSOURI CITY",
    "MORGANS POINT",
    "NASSAU BAY",
    "PASADENA",
    "PEARLAND",
    "PINEY POINT VILLAGE",
    "SEABROOK",
    "SHOREACRES",
    "SOUTH HOUSTON",
    "SOUTHSIDE PLACE",
    "SPRING",
    "SPRING VALLEY VILLAGE",
    "STAFFORD",
    "TAYLOR LAKE VILLAGE",
    "TOMBALL",
    "WALLER",
    "WEBSTER",
    "WEST UNIVERSITY PLACE",

    //Census-designated places
    "ALDINE",
    "ATASCOCITA",
    "BARRETT",
    "CHANNELVIEW",
    "CINCO RANCH",
    "CLOVERLEAF",
    "CROSBY",
    "HIGHLANDS",
    "MISSION BEND",
    "SHELDON",
    "SPRING"
  };
}
