package net.anei.cadpage.parsers.WA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WAGraysHarborCountyAParser extends DispatchA19Parser {

  public WAGraysHarborCountyAParser() {
    super(CITY_CODES, "GRAYS HARBOR COUNTY", "WA");
  }

  @Override
  public String getFilter() {
    return "noreply@gh911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("Rip-n-run Report") && !subject.equals("RipnRun Notice")) return false;

    int pt = body.indexOf("\n\n");
    if (pt >= 0 && body.substring(0,pt).equals("*** NEW RAPID NOTIFICATION REPORT ***")) {
      body = body.substring(pt+2).trim();
    }

    int pt1 = body.indexOf("\n Nature:") + 1;
    if (pt1 <= 0) return false;
    int pt2 = body.indexOf("\nCross Streets:", pt1) + 1;
    if (pt2 <= 0) return false;
    int pt3 = body.indexOf("\nINCIDENT # ", pt2) + 1;
    if (pt3 <= 0) return false;
    int pt4 = body.indexOf("\nResponding Units:", pt3) + 1;
    if (pt4 <= 0) return false;
    int pt5 = body.indexOf("\nComments:", pt4) + 1;
    if (pt5 <= 0) return false;
    body = body.substring(0,pt1) +
           body.substring(pt3,pt4) +
           body.substring(pt1,pt2) +
           body.substring(pt4,pt5) +
           body.substring(pt2,pt3) +
           body.substring(pt5);
    return super.parseMsg(subject, body, data);
  }

  private static Properties CITY_CODES = buildCodeTable(new String[] {

      // Cities
      "ABD", "ABERDEEN",
      "COS", "COSMOPOLIS",
      "ELM", "ELMA",
      "HOQ", "HOQUIAM",
      "MCC", "MCCLEARY",
      "MON", "MONTESANO",
      "OAK", "OAKVILLE",
      "OCC", "OCEAN SHORES",
      "OCS", "OCEAN SHORES",
      "WES", "WESTPORT",

/*
  Census-designated places

      ABERDEEN GARDENS
      BRADY
      CENTRAL PARK
      CHEHALIS VILLAGE
      COHASSETT BEACH
*/
      "COB", "COPALIS BEACH",
      "GRY", "GRAYLAND",
/*
      HUMPTULIPS
      JUNCTION CITY
      */
      "MAL", "MALONE-PORTER",
      "POR", "ELMA",
      /*
      MARKHAM
  */
      "MOL", "MOCLIPS",
      /*
      NEILTON
      OCEAN CITY
      OYEHUT-HOGANS CORNER
      SATSOP
      */
      "SAT", "ELMA",
      "TAH", "TAHOLAH",

      /*
  Other communities

      ALDER GROVE
      AMANDA PARK
      BAY CITY
      CARLISLE
      CHENOIS CREEK
*/
      "COC", "COPALIS CROSSING",
      /*
      DECKERVILLE
      GARDEN CITY
      GRAY GABLES
      GRAYS HARBOR CITY
      GRISDALE
      HEATHER
      MELBOURNE
      NEW LONDON
      NEWTON
      NISSON
*/
      "NOC", "NORTH COVE",
/*
      OCOSTA
      */
      "PAB", "PACIFIC BEACH",
      /*
      PACIFIC-SEABROOK
      PREACHERS SLOUGH
      QUINAULT
      SAGINAW
      SOUTH ELMA
      SOUTH MONTESANO
*/
  });

}
