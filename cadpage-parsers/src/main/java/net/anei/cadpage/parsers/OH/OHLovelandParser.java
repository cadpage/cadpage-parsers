package net.anei.cadpage.parsers.OH;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA1Parser;

public class OHLovelandParser extends DispatchA1Parser {
  
  public OHLovelandParser() {
    this("LOVELAND", "OH");
  }
  
  protected OHLovelandParser(String defCity, String defState) {
    super(CITY_CODES, defCity, defState);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    addExtendedDirections();
  }
  
  @Override
  public String getAliasCode() {
    return "OHLoveland";
  }
  
  @Override
  public String getFilter() {
    return "dispatcher@safety-center.org,utcc@union-township.oh.us,dispatcher@lsfd.org,BrownCommCtr@BrownCountyOhio.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    body = stripFieldStart(body, "AUTOMATED MESSAGE DO NOT REPLY\n");
    if (subject.length() == 0)
      if (body.startsWith("Alert:")) {
        int pt = body.indexOf('\n');
        if (pt < 0) return false;
        subject = body.substring(0, pt).trim();
        body = body.substring(pt+1).trim();
      } else if (body.startsWith("LOC:")) {
        subject = "Alert:";
    }
    return super.parseMsg(subject, body, data);
  }

  @Override
  public String adjustMapAddress(String addr) {
    return TR_PTN.matcher(addr).replaceAll("TRACE");
  }
  private static final Pattern TR_PTN = Pattern.compile("\\bTR\\b", Pattern.CASE_INSENSITIVE);

  
  
  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }
  
  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "53 I 275 NORTH",                       "+39.238241,-84.296560",
      "54 I 275 NORTH",                       "+39.228164,-84.283266",
      "55 I 275 NORTH",                       "+39.217587,-84.269905",
      "56 I 275 NORTH",                       "+39.206050,-84.262929",
      "57 I 275 NORTH",                       "+39.191219,-84.262434",
      "58 I 275 NORTH",                       "+39.176348,-84.267205",
      "59 I 275 NORTH",                       "+39.163891,-84.267453",
      "60 I 275 NORTH",                       "+39.149671,-84.266032",
      "61 I 275 NORTH",                       "+39.137734,-84.264689",
      "62 I 275 NORTH",                       "+39.124028,-84.273669",
      "63 I 275 NORTH",                       "+39.107801,-84.280230",
      "64 I 275 NORTH",                       "+39.094263,-84.288279",
      "65 I 275 NORTH",                       "+39.079017,-84.290910",
      "66 I 275 NORTH",                       "+39.066785,-84.301530",
      "I 275 NORTH EXIT HWY 28",              "+39.181806,-84.265106",
      "I 275 NORTH EXIT SR 125",              "+39.065487,-84.303932",
      "I 275 NORTH EXIT SR 32",               "+39.095971,-84.287517",
      "I 275 NORTH EXIT SR 450",              "+39.154458,-84.266648",
      "I 275 NORTH EXIT WARDS CORNER RD",     "+39.222340,-84.274367",
      "53 I 275 SOUTH",                       "+39.238074,-84.296833",
      "54 I 275 SOUTH",                       "+39.227969,-84.283493",
      "55 I 275 SOUTH",                       "+39.217471,-84.270206",
      "56 I 275 SOUTH",                       "+39.206026,-84.263259",
      "57 I 275 SOUTH",                       "+39.191217,-84.262778",
      "58 I 275 SOUTH",                       "+39.176406,-84.267536",
      "59 I 275 SOUTH",                       "+39.163886,-84.267714",
      "60 I 275 SOUTH",                       "+39.149671,-84.266369",
      "61 I 275 SOUTH",                       "+39.137744,-84.265014",
      "62 I 275 SOUTH",                       "+39.124144,-84.273963",
      "63 I 275 SOUTH",                       "+39.107868,-84.280549",
      "64 I 275 SOUTH",                       "+39.094306,-84.288603",
      "65 I 275 SOUTH",                       "+39.079070,-84.291247",
      "66 I 275 SOUTH",                       "+39.066997,-84.301747",
      "I 275 SOUTH EXIT HWY 28",              "+39.192060,-84.262792",
      "I 275 SOUTH EXIT SR 125",              "+39.072501,-84.295378",
      "I 275 SOUTH EXIT SR 32",               "+39.106582,-84.281196",
      "I 275 SOUTH EXIT SR 450",              "+39.166585,-84.267900",
      "I 275 SOUTH EXIT WARDS CORNER RD",     "+39.227373,-84.282518",
      "1 SR 32 EAST",                         "+39.104259,-84.292725",
      "2 SR 32 EAST",                         "+39.098374,-84.274544",
      "3 SR 32 EAST",                         "+39.093102,-84.257155",
      "4 SR 32 EAST",                         "+39.088748,-84.239468",
      "5 SR 32 EAST",                         "+39.088489,-84.220864",
      "6 SR 32 EAST",                         "+39.086242,-84.200870",
      "7 SR 32 EAST",                         "+39.084488,-84.185628",
      "8 SR 32 EAST",                         "+39.086292,-84.169321",
      "9 SR 32 EAST",                         "+39.083101,-84.149954",
      "10 SR 32 EAST",                        "+39.078460,-84.131896",
      "11 SR 32 EAST",                        "+39.074126,-84.115028",
      "12 SR 32 EAST",                        "+39.070604,-84.096826",
      "13 SR 32 EAST",                        "+39.067739,-84.079207",
      "14 SR 32 EAST",                        "+39.067022,-84.057544",
      "15 SR 32 EAST",                        "+39.063951,-84.041562",
      "16 SR 32 EAST",                        "+39.062105,-84.023372",
      "SR 32 EAST EXIT EASTGATE BV",          "+39.100564,-84.281177",
      "SR 32 EAST EXIT HALF ACRE RD",         "+39.070750,-84.099343",
      "SR 32 EAST EXIT I 275",                "+39.103089,-84.288868",
      "SR 32 EAST EXIT JAMES E SAULS SR DR",  "+39.076584,-84.124594",
      "SR 32 EAST EXIT MAIN ST",              "+39.084179,-84.194075",
      "SR 32 EAST EXIT OLIVE BRANCH SL",      "+39.088575,-84.227266",
      "SR 32 EAST EXIT SR 133",               "+39.067024,-84.067634",
      "SR 32 EAST EXIT SR 222",               "+39.085258,-84.180215",
      "1 SR 32 WEST",                         "+39.104422,-84.292652",
      "2 SR 32 WEST",                         "+39.098537,-84.274447",
      "3 SR 32 WEST",                         "+39.093266,-84.257083",
      "4 SR 32 WEST",                         "+39.088920,-84.239470",
      "5 SR 32 WEST",                         "+39.088659,-84.220877",
      "6 SR 32 WEST",                         "+39.086346,-84.200800",
      "7 SR 32 WEST",                         "+39.084606,-84.185638",
      "8 SR 32 WEST",                         "+39.086410,-84.169327",
      "9 SR 32 WEST",                         "+39.083313,-84.149868",
      "10 SR 32 WEST",                        "+39.078668,-84.131777",
      "11 SR 32 WEST",                        "+39.074338,-84.114922",
      "12 SR 32 WEST",                        "+39.070381,-84.096864",
      "13 SR 32 WEST",                        "+39.067964,-84.079161",
      "14 SR 32 WEST",                        "+39.067253,-84.057546",
      "15 SR 32 WEST",                        "+39.064183,-84.041529",
      "16 SR 32 WEST",                        "+39.062332,-84.023349",
      "SR 32 WEST EXIT BATAVIA RD",           "+39.074163,-84.114236",
      "SR 32 WEST EXIT EASTGATE BV",          "+39.096849,-84.269295",
      "SR 32 WEST EXIT HALF ACRE RD",         "+39.069261,-84.087837",
      "SR 32 WEST EXIT I275",                 "+39.099850,-84.278443",
      "SR 32 WEST EXIT OLIVE BRANCH SL",      "+39.088592,-84.216144",
      "SR 32 WEST EXIT SR 132",               "+39.086413,-84.168682",
      "SR 32 WEST EXIT SR 133",               "+39.067261,-84.057757"
  });
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "LOVELD", "LOVELAND",
      "SYMMTP", "SYMMES TWP"
  });
}
