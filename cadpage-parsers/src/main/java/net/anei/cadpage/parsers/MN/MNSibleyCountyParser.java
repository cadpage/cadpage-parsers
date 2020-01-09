package net.anei.cadpage.parsers.MN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class MNSibleyCountyParser extends DispatchProQAParser {
  
  public MNSibleyCountyParser() {
    super(CITY_LIST, "SIBLEY COUNTY", "MN", 
          "ID! ADDR ( CITY/Z ZIP | CITY EMPTY | PLACE APT CITY ZIP ) CALL! CALL/L+", true);
  }
  
  @Override
  public String getFilter() {
    return "john.scheuch@ridgeviewmedical.org";
  }
   
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("Run#")) body = "RC:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d\\d-\\d+");
    if (name.equals("ZIP")) return new MyZipField();
    return super.getField(name);
  }
  
  private static final Pattern ZIP_PTN = Pattern.compile("\\d{5}");
  private class MyZipField extends CityField {
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!ZIP_PTN.matcher(field).matches()) return false;
      if (data.strCity.length() == 0) data.strCity = field;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final String[] CITY_LIST = new String[]{


      // Cities
      "ARLINGTON",
      "GAYLORD",
      "GIBBON",
      "GREEN ISLE",
      "HENDERSON",
      "LE SUEUR",
      "NEW AUBURN",
      "WINTHROP",

      // Unincorporated communities
      "ASSUMPTION",
      "NEW ROME",
      "RUSH RIVER",

      // Townships
      "ALFSBORG TOWNSHIP",
      "ARLINGTON TOWNSHIP",
      "BISMARCK TOWNSHIP",
      "CORNISH TOWNSHIP",
      "DRYDEN TOWNSHIP",
      "FAXON TOWNSHIP",
      "GRAFTON TOWNSHIP",
      "GREEN ISLE TOWNSHIP",
      "HENDERSON TOWNSHIP",
      "JESSENLAND TOWNSHIP",
      "KELSO TOWNSHIP",
      "MOLTKE TOWNSHIP",
      "NEW AUBURN TOWNSHIP",
      "SEVERANCE TOWNSHIP",
      "SIBLEY TOWNSHIP",
      "TRANSIT TOWNSHIP",
      "WASHINGTON LAKE TOWNSHIP",
      
      // Carver County
      "WACONIA"

     
  };
}
