package net.anei.cadpage.parsers.NJ;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchProphoenixParser;

public class NJMonmouthCountyCParser extends DispatchProphoenixParser {

  public NJMonmouthCountyCParser() {
    super(CITY_CODES, NJMonmouthCountyParser.CITY_LIST, "MONMOUTH COUNTY", "NJ");
  }

  @Override
  public String getFilter() {
    return "ejs1396@gmail.com,ubpolice@unionbeachnj.gov";
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "UP", "UNION BEACH"
  });
}
