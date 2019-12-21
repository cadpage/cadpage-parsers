package net.anei.cadpage.parsers.ZSE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZSESwedenAParser extends ZSESwedenBaseParser {

  public ZSESwedenAParser() {
    super("", "", 
    "( FROM!  Radiogruppsnamn:CH! ADDR PLACE CITY PLACE? CALL! CALL CALL INFO/N+? Kompletterande_kategoritext:INFO! INFO/N+ " +
    "| ( ID | CH ID ) ( CH ( CH CH+? CALL CALL+? | CALL CALL ) | CALL CALL CALL ) Händelsebeskrivning:INFO? INFO2? ADDR CITY! SRC Larmkategori_namn:PRI? PositionWGS84:GPS! Stationskod:SKIP? Larmkategori_namn:PRI? Händelsebeskrivning:INFO INFO+ " + 
    "| CALL CALL CALL ADDR! CITY ( END | GPS INFO/N+ | INFO+? SRC UNIT! UNIT/S+? GPS ) ) END");
  }
  
  @Override
  public String getFilter() {
    return "3315,46730120451,@zenit.sosalarm.se,Larmserver@kallareetuna.se";
  }

  @Override
  public String getLocName() {
    return "Sweden";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), 4, data)) return false;
    if (data.strCity.equals("-")) data.strCity = "";
    if (data.strPlace.equals(data.strCity)) data.strPlace = "";
    
    // Comment out following line to check call table
    if (data.strGPSLoc.length() > 0) return true;
    
    String call = data.strCall;
    if (call.length() == 0) return true;
    int pt = call.indexOf(" / ");
    if (pt >= 0) call = call.substring(0,pt).trim();
    return CALL_SET.contains(call);
  };
  
  @Override
  public Field getField(String name) {
    if (name.equals("FROM")) return new SourceField("Från +(.*)", true);
    if (name.equals("ID")) return new IdField("\\d+");
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("INFO2")) return new ExtraInfoField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("SRC")) return new SourceField("\\d{3}-\\d{4}");
    if (name.equals("UNIT")) return new UnitField("\\d{3}-\\d{4}|MRSYDLB|MRSYDIB");
    return super.getField(name);
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (data.strPlace.equals(data.strCity)) data.strPlace = "";
      super.parse(field, data);
    }
  }
  
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!CALL_SET.contains(field)) return false;
      parse(field, data);
      return true;
    }
  }
  
  // Sometimes the info field is split by a newline leaving a fragment that we have to
  // identify and merge back into the original info field. Detecting this situation is
  // a bit tricky.  Best bet is to look ahead to see if the source field is what it should
  // be if this is an extra field
  private static final Pattern INFO_SRC_PTN = Pattern.compile("D\\d+");
  private class ExtraInfoField extends MyInfoField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (data.strSupp.length() == 0) return false;
      String src = getRelativeField(3);
      if (!INFO_SRC_PTN.matcher(src).matches()) return false;
      parse(field, data);
      return true;
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("")) return;
      if (isValidChannel(field)) {
        addChannel(field, data);
      } else {
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CH INFO";
    }
  }
  
  private static final Pattern APT_MARKER = Pattern.compile("(?: Nr(?= |\\d)|,) *(\\d+[A-Z]?\\b)? *(.*)$");
  private static final Pattern PROPERTY_MARKER = Pattern.compile("\\w+:.*");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_MARKER.matcher(field);
      if (match.find()) {
        field = field.substring(0,match.start()).trim();
        data.strApt = getOptGroup(match.group(1));
        String place = match.group(2);
        if (place.startsWith(";")) place = place.substring(1).trim();
        if (PROPERTY_MARKER.matcher(place).matches()) {
          data.strSupp = place;
        } else {
          data.strPlace = place;
        }
      }
      data.strAddress = field;
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE INFO?";
    }
  }
  
  private static final Set<String> CALL_SET = new HashSet<>(Arrays.asList(new String[]{
      "--",
      "Ambulansassistans",
      "Annat",
      "Automatlarm",
      "Båtolycka",
      "Brand",
      "Brand i byggnad",
      "Brand ute - container",
      "Brand ute - fordon",
      "Brand ute - övrigt",
      "Brand ute - terräng",
      "Djurräddning",
      "DRH Test",
      "Driftlarm",
      "Drunkning",
      "Dykolycka",
      "eCall utan talkontakt",
      "Explosion",
      "Fastklämd",
      "Flyghändelse",
      "Gräsbrand",
      "Hjärtstopp",
      "Hinder på väg",
      "Hissnödläge",
      "Hot om suicid",
      "Inbrott",
      "Interna brandlarm",
      "Interna brandlarm Via annan larmcentral(W)",
      "Järnväg - brand",
      "Järnväg - kollision/urspårning",
      "Järnväg - övrigt",
      "Järnväg - påkörd person",
      "Lösa föremål",
      "Olycka - trafik",
      "Olyckor (Trauma)",
      "Övrigt",
      "Övrigt räddning",
      "Pågående dödligt våld",
      "Påkörd person",
      "Person i vatten",
      "Person - svår belägenhet",
      "Polis Räddning",
      "R��ddning provlarm",
      "Räddning - övrigt",
      "Räddning provlarm",
      "Ras/skred",
      "Restvärde",
      "Röklukt - byggnad",
      "Röklukt - ute",
      "Sensorlarm utan talkontakt",
      "Sjukvårdslarm",
      "Soteld",
      "Tekniskt fel",
      "Trafikolycka - flera fordon",
      "Trafikolycka - mindre motorfordon",
      "Trafikolycka - påkörd person",
      "Trafikolycka - singel",
      "Utsläpp",
      "Utsläpp/Explosion",
      "Utsläpp farligt ämne - drivmedel",
      "Utsläpp farligt ämne - gas",
      "Utsläpp farligt ämne - övrigt",
      "Vädervarning",
      "Vattenskada"
  }));
}
