package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.GroupBestParser;


public class NYNiagaraCountyParser extends GroupBestParser {
  
  public NYNiagaraCountyParser() {
    super(new NYNiagaraCountyAParser(),
          new NYNiagaraCountyBParser(),
          new NYNiagaraCountyCParser());
  }
}
