package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class WAGraysHarborCountyAParser extends FieldProgramParser {

  public WAGraysHarborCountyAParser() {
    super("GRAYS HARBOR COUNTY", "WA",
      "CALL ( Xst:X | EMPTY ) ID!");
  }

  @Override
  public String getProgram() {
    return "UNIT CODE ADDR APT CITY PLACE " + super.getProgram();
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    subject = subject.split("\\|")[0];
    if (!parseSubject(subject, data)) return false;
    if (data.strCode.equals("")) {
      setFieldList("INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strSupp = body;
      return true;
    }
    return parseFields(body.split("\n"), data);
  }

  private static final Pattern SUBJECT_PATTERN
  = Pattern.compile("([^-]+?) - ?([^-]*?) - ?(.+?)(?:--(.*))?");
  private boolean parseSubject(String subject, Data data) {
    Matcher m = SUBJECT_PATTERN.matcher(subject);
    if (m.matches()) {
      data.strUnit = m.group(1).trim();
      data.strCode = m.group(2).trim();
      if (data.strCode.length() > 0) {
        String addr = m.group(3).trim();
        data.strPlace = getOptGroup(m.group(4));
        
        int pt = addr.lastIndexOf(',');
        if (pt >= 0) {
          data.strCity = convertCodes(addr.substring(pt+1).trim(), CITY_CODES);
          addr = addr.substring(0,pt).trim();
        }
        parseAddress(addr, data);
      }
      return true;
    }
    return false;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("#([A-Z]{2}\\d{5} \\d{4})", true);
    return super.getField(name);
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
