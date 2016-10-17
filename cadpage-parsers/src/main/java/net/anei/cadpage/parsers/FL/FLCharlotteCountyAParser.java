package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA24Parser;


/**
 * Charlotte County, FL
 */
public class FLCharlotteCountyAParser extends DispatchA24Parser {
  
  public FLCharlotteCountyAParser() {
    super("CHARLOTTE COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "administrator@ccsofl.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CCSO FIRE CALL")) return false;
    return parseMsg(body, data);
  }
  
  private static final Pattern INFO_GPS1_PTN = Pattern.compile("\\bLAT (\\d{3}\\.\\d{6}) +LOG (\\d{3}\\.\\d{6})\\b");
  private static final Pattern INFO_GPS2_PTN = Pattern.compile("\\bLAT (\\d{2,3}\\.\\d\\d\\.\\d\\d) [NS] LON?G (\\d{2,3}\\.\\d\\d\\.\\d\\d) [EW]\\b");
  private static final Pattern INFO_BRACKET_PTN = Pattern.compile(" *(?:\\[[^\\]]*\\] *| *\\.{2,} *|  +)+");
  private static final Pattern SKIP_PTN = Pattern.compile("^\\(Cloned from.*?\\) *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS1_PTN.matcher(field);
      if (match.find()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
        field = field.substring(0,match.start()) + "  " + field.substring(match.end());
      }
      else {
        match = INFO_GPS2_PTN.matcher(field);
        if (match.find()) {
          setGPSLoc(match.group(1).replace('.',' ')+','+match.group(2).replace('.',' '), data);
          field = field.substring(0,match.start()) + "  " + field.substring(match.end());
        }
        
      }
      
      for (String fld : INFO_BRACKET_PTN.split(field)) {
        fld = SKIP_PTN.matcher(fld).replaceAll("");
        if (fld.startsWith("Cross streets:")) {
          String cross = fld.substring(14).trim().replace("//", "/");
          if (cross.endsWith("/")) cross = cross.substring(0,cross.length()-1).trim();
          data.strCross = append(data.strCross, "/", cross);
        }
        else if (fld.startsWith("Landmark:")) {
          fld = fld.substring(9).trim();
          addPlace(fld, data);
        }
        else if (fld.startsWith("NBH:")) {
          fld = fld.substring(4).trim();
          addPlace(fld, data);
        }
        else {
          if (fld.startsWith("Landmark Comment:")) fld = fld.substring(17).trim();
          else if (fld.startsWith("Geo Comment:")) fld = fld.substring(12).trim();
          if (fld.endsWith(",") || fld.endsWith(".")) fld = fld.substring(0,fld.length()-1).trim();
          data.strSupp = append(data.strSupp, " / ", fld);
        }
      }
    }
    
    private void addPlace(String place, Data data) {
      if (data.strPlace.contains(place)) return;
      data.strPlace = append(data.strPlace, " - ", place);
    }
    
    @Override
    public String getFieldNames() {
      return "X PLACE INFO GPS";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
