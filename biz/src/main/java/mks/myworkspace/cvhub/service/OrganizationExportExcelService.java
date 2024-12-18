package mks.myworkspace.cvhub.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import mks.myworkspace.cvhub.entity.Organization;
public interface OrganizationExportExcelService {
    ByteArrayOutputStream createExcelFile(List<Organization> organizations) throws IOException;
}