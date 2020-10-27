package net.anei.cadpage.parsers.PA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class PAAlleghenyCountyCParser extends DispatchProQAParser {
  
  public PAAlleghenyCountyCParser() {
    super("ALLEGHENY COUNTY", "PA", 
          "CALL_PREFIX? CALL/L CALL2/SLS+? EMPTY+? ADDR PLACE_APT EMPTY/Z? CITY ID! PRI! INFO/L+", true);
    setupGpsLookupTable(PAAlleghenyCountyParser.GPS_TABLE_LOOKUP);
    setupPlaceGpsLookupTable(PAAlleghenyCountyParser.PLACE_GPS_LOOKUP_TABLE);
  }
  
  private static final Pattern MBLANK_PTN = Pattern.compile(" {3,}");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = convertCodes(data.strCity.toUpperCase(), FIX_CITY_TABLE);
    data.strSupp = MBLANK_PTN.matcher(data.strSupp).replaceAll(" / ");
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_PREFIX")) return new CallField("EMERGENCY|ALS-\\d|BLS|BLS Non Covered Ambulance", true);
    if (name.equals("CALL2"))  return new MyCall2Field();
    if (name.equals("PLACE_APT")) return new MyPlaceAptField();
    return super.getField(name);
  }
  
  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!CALL_EXT_SET.contains(field)) return false;
      super.parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern PLACE_APT_PTN1 = Pattern.compile("(?:(.*) )?(?:APT|RM|ROOM|LOT)\\b[#. ]*+(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern PLACE_APT_PTN2 = Pattern.compile("(?!EXIT)(?:(.*) )?(\\d+(?:-?[A-Za-z])?)");
  private class MyPlaceAptField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_APT_PTN1.matcher(field);
      if (!match.matches()) {
        match = PLACE_APT_PTN2.matcher(field);
        if (!match.matches()) match = null;
      }
      if (match != null) {
        data.strApt = append(data.strApt, "-", match.group(2));
        field = getOptGroup(match.group(1));
      }
      data.strPlace = field;
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }
  
  private static final Set<String> CALL_EXT_SET = new HashSet<String>(Arrays.asList(new String[]{
      "Near Fainting"      
  }));
  
  private static final Properties FIX_CITY_TABLE = buildCodeTable(new String[]{
      "ALLEPO",     "ALEPPO",
      "COR",        "CORAOPOLIS"
  });
}
