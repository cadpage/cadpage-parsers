package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class MOStLouisCountyJParser extends DispatchProQAParser {
  
  public MOStLouisCountyJParser() {
    super(MOStLouisCountyParser.CITY_LIST, "ST LOUIS COUNTY", "MO",
          "PRI! CALL! PLACE! PLACE+? TIME INFO/SLS+? ID! ADDR! INFO2+", true);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO2")) return new MyInfo2Field();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      data.strPlace = append(data.strPlace, "/", field);
    }
  }

  private static final Pattern INFO_APT_PTN = Pattern.compile("(?:APT|RM|LOT)[- ]+(.*)|ED[- ]+.*", Pattern.CASE_INSENSITIVE);
  private class MyInfo2Field extends Field {
    @Override
    public void parse(String field, Data data) {
      if (data.strApt.length() == 0) {
        Matcher match = INFO_APT_PTN.matcher(field);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = field;
          data.strApt = append(data.strApt, "-", apt);
          return;
        }
      }
      
      if (data.strCity.length() == 0 && isCity(field)) {
        data.strCity = field;
        return;
      }
      
      data.strSupp = append(data.strSupp, " / ", field);
    }

    @Override
    public String getFieldNames() {
      return "APT CITY INFO";
    }
  }

}
