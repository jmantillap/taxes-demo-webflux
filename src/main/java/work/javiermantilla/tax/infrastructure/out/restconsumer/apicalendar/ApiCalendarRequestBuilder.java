package work.javiermantilla.tax.infrastructure.out.restconsumer.apicalendar;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiCalendarRequestBuilder {

    static void addCustomHeaders(HttpHeaders httpHeaders) {
        httpHeaders.add("date", new Date().toString());
    }

    /*public static FileDataWasRequestDTO buildFileRequestDTO(DownloadFile downloadFile) {

        var dataDTO = FileDataWasRequestDTO.DataDTO.builder().build();
        var accountInformationDTO =FileDataWasRequestDTO.DataDTO.AccountInformationDTO.builder()
                .accountNumber(downloadFile.getData().getAccountIformation().getAccountNumber())
                .accType(downloadFile.getData().getAccountIformation().getAccType())
                .accTypeCode(downloadFile.getData().getAccountIformation().getAccTypeCode())
                .build();
        var customTemplateDTO = FileDataWasRequestDTO.DataDTO.CustomTemplateDTO.builder()
                .formatName(downloadFile.getData().getCustomTemplate().getFormatName())
                .transactionCode(downloadFile.getData().getCustomTemplate().getTransactionCode())
                .date(downloadFile.getData().getCustomTemplate().getDate())
                .typeFormat(getTypeFormatDTO(downloadFile.getData().getCustomTemplate().getTypeFormat()))
                .fields(getFieldsDTO(downloadFile.getData().getCustomTemplate().getFields()))
                .build();
        dataDTO.setAccountInformation(accountInformationDTO);
        dataDTO.setCustomTemplate(customTemplateDTO);

        dataDTO.setFilter(FileDataWasRequestDTO.DataDTO.FilterDownloadDTO.builder()
                .dateFrom(downloadFile.getData().getFilter().getDateFrom())
                .dateEnd(downloadFile.getData().getFilter().getDateEnd())
                .transactionType(downloadFile.getData().getFilter().getTransactionType())
                .build());

        return FileDataWasRequestDTO.builder()
                .data(dataDTO)
                .build();

    }

    private static List<FileDataWasRequestDTO.DataDTO.CustomTemplateDTO
            .FieldDTO> getFieldsDTO(ArrayList<Field> fields) {
        List<FileDataWasRequestDTO.DataDTO.CustomTemplateDTO.FieldDTO> fieldsDTO = new ArrayList<>();
        fields.forEach(field ->
                fieldsDTO.add(FileDataWasRequestDTO.DataDTO.CustomTemplateDTO.FieldDTO.builder()
                .id(field.getId())
                .name(field.getName())
                .order(field.getOrder())
                .build()));
        return fieldsDTO;
    }

    private static FileDataWasRequestDTO.DataDTO.CustomTemplateDTO
            .TypeFormatDownloadDTO getTypeFormatDTO(TypeFormatDownload typeFormat) {
        return FileDataWasRequestDTO.DataDTO.CustomTemplateDTO.TypeFormatDownloadDTO.builder()
                .id(typeFormat.getId())
                .name(typeFormat.getName())
                .description(typeFormat.getDescription())
                .delimiter(typeFormat.getDelimiter())
                .legacy(typeFormat.getLegacy())
                .format(Optional.ofNullable(typeFormat.getFormat())
                        .filter(v -> !v.isEmpty())
                        .orElse("PER"))
                .build();

    }*/


}
