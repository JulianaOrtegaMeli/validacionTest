package com.mercadolibre.validator.util;


import com.google.gson.Gson;
import com.mercadolibre.validator.dto.InputExcelDTO;
import com.mercadolibre.validator.dto.InputJsonDTO;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.mercadolibre.validator.service.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("RegexUtils")
//@Profile({"local"})
public class AutomateTestFilterableUtils {

  static final String BASE_URL = "http://localhost:8080/validation-hub/items/normalize";

  static final List<String> ID_AGE_GROUP = Arrays.asList("6725189", "1065183");

  static final List<String> ID_GENDER = Arrays.asList("339665", "339666", "339667", "339668", "110461", "371795");

  static final List<String> HEADERS_EXCEL = Arrays.asList("GENDER", "AGE GROUP", "SIZE",
      "EXPECTED FILTRABLE_SIZE", "ACTUAL FILTRABLE_SIZE", "COINCIDE");

  static final int HTTP_DEFAULT_VALUE = 3000;

  static final String NO_EXISTE = "No existe";

  static final int WIDTH_COLUMN = 4200;

  static Logger logger = LoggerFactory.getLogger(AutomateTestFilterableUtils.class);

  static String nameFileOut = " out.xlsx";

  String fileName;

  FileOutputStream outputStream;

  public AutomateTestFilterableUtils() {

  }

  /**
   * Método que lee el excel de entrada y llama a los otros método de procesamiento.
   *
   * @param domain Es el domain a procesar (T_SHIRTS, PANTS, SNEAKERS, ...)
   * @param site Es el site a procesar (MLA, MLB, MLM, ...)
   * @param pathInput Es la ruta del archivo de excel a leer
   * @param pathOutput Es la ruta donde se escribira el archivo excel resultante
   */
  public void automateTest(String domain, String site, String pathInput, String pathOutput) {
    fileName = site + " " + domain + nameFileOut;
    File fileInput = new File(pathInput);
    String path = fileInput.getPath();

    try (Closeable hssfWorkbookOut = new XSSFWorkbook()) {
      createExcelOut(site, hssfWorkbookOut);
      readExcel(domain, site, path, hssfWorkbookOut);
      writeExcelResult(hssfWorkbookOut, pathOutput);
    } catch (Exception e) {
      logger.error("Ocurrio un error al crear el nuevo archivo xlsx resultante");
    }

  }

  private void readExcel(String domain, String site, String path, Closeable hssfWorkbookOut) {
    try (Closeable hssfWorkbook = new XSSFWorkbook(Files.newInputStream(Paths.get(path)))) {
      XSSFSheet pageActual;
      //Column
      //FOR HOJAS
      for (int sheetPosition = 1; sheetPosition < ((XSSFWorkbook) hssfWorkbook).getNumberOfSheets(); sheetPosition++) {
        //page actual
        pageActual = ((XSSFWorkbook) hssfWorkbook).getSheetAt(sheetPosition);
        if (pageActual == null) {
          continue;
        }
        readRowsExcel(site, domain, pageActual, hssfWorkbookOut);
      }

    } catch (Exception e) {
      logger.error("Ocurrio un error al procesar el archivo xlsx");
    }
  }

  private void readRowsExcel(String site, String domain, XSSFSheet pageActual, Closeable hssfWorkbookOut) {

    XSSFRow row;
    for (int rowPosition = 0; rowPosition <= pageActual.getLastRowNum(); rowPosition++) {
      //page actual
      if (rowPosition == 0) {
        continue;
      }
      row = pageActual.getRow(rowPosition);
      if (null == row) {
        break;
      }
      String siteSheet = pageActual.getSheetName();
      if (siteSheet.equalsIgnoreCase(site)) {
        processData(site, domain, rowPosition, pageActual, hssfWorkbookOut);
      }
    }
  }

  private void processData(String site, String domain, int rowPosition, XSSFSheet pageActual, Closeable hssfWorkbookOut) {
    InputExcelDTO excelDTO = createExcelDTO(pageActual, rowPosition);
    InputJsonDTO jsonDTO = excelDTOtoJsonDTO(excelDTO, site, domain);
    generateJson(jsonDTO);
    String resultHttp = getFilterableSize(generateJson(jsonDTO));
    setContentExcel(hssfWorkbookOut, excelDTO, rowPosition, resultHttp);
  }

  private void writeExcelResult(Closeable hssfWorkbookOut, String pathOutput) {
    try {
      outputStream = new FileOutputStream(pathOutput + fileName);
      ((XSSFWorkbook) hssfWorkbookOut).write(outputStream);
      ((XSSFWorkbook) hssfWorkbookOut).close();
    } catch (Exception e) {
      logger.error("Ocurrio un error al escribir el archivo xlsx");
    }
  }

  /**
   * Método para obtener la respuesta del consumo http del collider.
   * @param jsonString Es el dto que contiene la información del json a enviar
   * @return String valor del filterableSize
   */
  public String getFilterableSize(String jsonString) {

    //OutputJsonDTO resultDto = restClient.searchFilterableSize(jsonDTO);
    //return resultDto.getCauses().get(0).getMessage();
    return TestValidation.invokeApiCall(jsonString);
  }

  /**
   * Método que sirve para crear el excel de respuesta con la comparación del filtrable_size esperado y obtenido.
   * @param site Es el site a procesar (MLA, MLB, MLM, ...)
   * @param hssfWorkbookOut Es el nuevo archivo de excel de salida
   */
  private void createExcelOut(String site, Closeable hssfWorkbookOut) {
    XSSFSheet sheet = ((XSSFWorkbook) hssfWorkbookOut).createSheet(site);
    setWidthHeaders(sheet);
    createHeadersExcel(sheet, createStyleHeader(sheet));
  }

  private void setWidthHeaders(XSSFSheet sheet) {
    for (int positionColumn = 0; positionColumn <= 6; positionColumn++) {
      sheet.setColumnWidth(positionColumn, WIDTH_COLUMN);
    }
  }

  private XSSFCellStyle createStyleHeader(XSSFSheet sheet) {
    XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
    XSSFFont font = sheet.getWorkbook().createFont();
    font.setFontHeight(12);
    font.setBold(true);
    style.setFont(font);
    return style;
  }

  /**
   * Método para editar el contenido del excel de salida.
   * @param hssfWorkbookOut Es el nuevo archivo de excel de salida
   * @param excelDTO Es el dto que contiene la información (genero, ageGroup, size, expected)
   * @param rowNum Es el número de la fila
   * @param resultHttp Es el resultado del consumo http al collider
   */
  private void setContentExcel(Closeable hssfWorkbookOut, InputExcelDTO excelDTO, int rowNum, String resultHttp) {
    XSSFSheet sheet = ((XSSFWorkbook) hssfWorkbookOut).getSheetAt(0);
    XSSFRow row = sheet.createRow(rowNum);

    XSSFCell cellGenderName = row.createCell(0, CellType.STRING);
    cellGenderName.setCellValue(calculateValueNameGender(excelDTO.getGender()));

    XSSFCell cellGender = row.createCell(1, CellType.STRING);
    cellGender.setCellValue(excelDTO.getGender());

    XSSFCell cellAgeGroup = row.createCell(2, CellType.STRING);
    cellAgeGroup.setCellValue(excelDTO.getAgeGroup());

    XSSFCell cellSize = row.createCell(3, CellType.STRING);
    cellSize.setCellValue(excelDTO.getSize());

    XSSFCell cellExpected = row.createCell(4, CellType.STRING);
    cellExpected.setCellValue(excelDTO.getExpectedFilterableSize());

    XSSFCell cellActual = row.createCell(5, CellType.STRING);
    cellActual.setCellValue(resultHttp);

    XSSFCell cellMatch = row.createCell(6, CellType.STRING);
    cellMatch.setCellValue(calculateMatch(excelDTO.getExpectedFilterableSize(), resultHttp));

  }

  /**
   * Método para calcular la respuesta de la igualdad entre la columna EXPECTED FILTRABLE_SIZE y la columna ACTUAL FILTRABLE_SIZE.
   * @param expected Es el valor de la columna EXPECTED FILTRABLE_SIZE
   * @param actual Es el valor de la columna ACTUAL FILTRABLE_SIZE
   * @return String
   */
  private String calculateMatch(String expected, String actual) {
    if (expected == null) {
      return "Por validar";
    } else {
      if (expected.equals(actual)) {
        return "Verdadero";
      } else {
        return "Falso";
      }
    }

  }

  /**
   * Método para generar el json de entrada al consumo http.
   * @param jsonDTO Es el dto que contiene la información del json a enviar
   * @return jsonResult
   */
  private String generateJson(InputJsonDTO jsonDTO) {
    Gson gson = new Gson();
    return gson.toJson(jsonDTO);
  }

  /**
   * Método para crear la fila del encabezado en el excel de salida.
   * @param sheet Es la hoja creada del excel de salida
   * @param style Es el estilo para el encabezado del excel de salida
   */
  private void createHeadersExcel(XSSFSheet sheet, XSSFCellStyle style) {

    XSSFRow firstRow = sheet.createRow(0);

    for (int positionCell = 1; positionCell <= 6; positionCell++) {
      XSSFCell cell = firstRow.createCell(positionCell, CellType.STRING);
      firstRow.getCell(positionCell).setCellValue(HEADERS_EXCEL.get(positionCell - 1));
      firstRow.getCell(positionCell).setCellStyle(style);
    }

  }

  /**
   * Método para obtener el valor de las celdas y crear el dto del excel.
   * @param pageActual Es la página actual del excel de donde se sacará la información
   * @param rowNum Es el número de la fila
   * @return InputExcelDTO
   */
  private InputExcelDTO createExcelDTO(XSSFSheet pageActual, int rowNum) {
    String gender = pageActual.getRow(rowNum).getCell(1).getStringCellValue();
    String ageGroup = pageActual.getRow(rowNum).getCell(2).getStringCellValue();
    String size = pageActual.getRow(rowNum).getCell(3).getStringCellValue();
    String expected = pageActual.getRow(rowNum).getCell(4).getStringCellValue();
    InputExcelDTO dto = new InputExcelDTO();
    dto.setGender(gender);
    dto.setAgeGroup(ageGroup);
    dto.setSize(size);
    dto.setExpectedFilterableSize(expected);
    dto.setOrden(rowNum);
    return dto;
  }

  /**
   * Método para convertir la información del excelDTO al jsonDTO.
   * @param excelDTO Es el dto que contiene la información (genero, ageGroup, size, expected)
   * @param site Es el site a procesar (MLA, MLB, MLM, ...)
   * @param domain Es el domain a procesar (T_SHIRTS, PANTS, SNEAKERS, ...)
   *
   * @return InputJsonDTO
   */
  private InputJsonDTO excelDTOtoJsonDTO(InputExcelDTO excelDTO, String site, String domain) {
    InputJsonDTO dto = new InputJsonDTO(site, getIdCategoryByDomainAndSite(site, domain));

    // Attribute age group
    dto.getAttributes().get(0).setValueId(excelDTO.getAgeGroup());
    dto.getAttributes().get(0).setValueName(calculateValueNameAgeGroup(excelDTO.getAgeGroup()));
    dto.getAttributes().get(0).getValues().get(0).setId(excelDTO.getAgeGroup());
    dto.getAttributes().get(0).getValues().get(0).setName(calculateValueNameAgeGroup(excelDTO.getAgeGroup()));

    // Attribute gender
    dto.getAttributes().get(1).setValueId(excelDTO.getGender());
    dto.getAttributes().get(1).setValueName(calculateValueNameGender(excelDTO.getGender()));
    dto.getAttributes().get(1).getValues().get(0).setId(excelDTO.getGender());
    dto.getAttributes().get(1).getValues().get(0).setName(calculateValueNameGender(excelDTO.getGender()));

    //Attribute - variation size
    dto.getVariations().get(0).getAttributeCombinations().get(0).setValueName(excelDTO.getSize());
    dto.getVariations().get(0).getAttributeCombinations().get(0).getValues().get(0).setName(excelDTO.getSize());

    return dto;
  }

  /**
   * Método para calcular el Age Group según su id.
   * @param idAgeGroup Es el código del age group
   * @return String age group
   */
  private String calculateValueNameAgeGroup(String idAgeGroup) {
    return (ID_AGE_GROUP.get(0).equalsIgnoreCase(idAgeGroup)) ? "ADULTOS" : "NIÑOS";
  }

  /**
   * Método para calcular el genero según su id.
   * @param idGender Es el código del gender
   * @return String gender
   */
  private String calculateValueNameGender(String idGender) {
    String gender = "";
    if (ID_GENDER.get(0).equalsIgnoreCase(idGender)) {
      gender = "Mujer";
    } else if (ID_GENDER.get(1).equalsIgnoreCase(idGender)) {
      gender = "Hombre";
    } else if (ID_GENDER.get(2).equalsIgnoreCase(idGender)) {
      gender = "Niños";
    } else if (ID_GENDER.get(3).equalsIgnoreCase(idGender)) {
      gender = "Niñas";
    } else if (ID_GENDER.get(4).equalsIgnoreCase(idGender)) {
      gender = "Sin genero";
    } else if (ID_GENDER.get(5).equalsIgnoreCase(idGender)) {
      gender = "Bebes";
    }
    return gender;
  }

  /**
   * Metódo para obtener el id de la categoria según el site y el domain.
   * @param domain Es el domain a procesar (T_SHIRTS, PANTS, SNEAKERS, ...)
   * @return idCategory
   */
  private String getIdCategoryByDomainAndSite(String site, String domain) {
    JSONObject siteJson = null;
    JSONObject domainJson = null;
    try {
      File file = new File("./configs/ConsolidadoIdCategory.json");
      Object siteObject = new JSONParser().parse(Files.newBufferedReader(Paths.get(file.getPath()), Charset.forName("UTF-8")));
      siteJson = (JSONObject) siteObject;
      Object domainObject = siteJson.get(site);
      domainJson = (JSONObject) domainObject;
      String value = domainJson.get(domain).toString();
      siteJson.clear();
      return value;
    } catch (Exception e) {
      logger.error("Ocurrio al leer el json", e);
      return NO_EXISTE;
    } finally {
      if (siteJson != null) {
        siteJson.clear();
      }
    }
  }
}
