// Copyright 2014 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.api.services.samples.dfareporting.misc;

import com.google.api.services.dfareporting.Dfareporting;
import com.google.api.services.dfareporting.model.ContentCategory;
import com.google.api.services.samples.dfareporting.DfaReportingFactory;

/**
 * This example creates a content category with the given name and description.
 */
public class CreateContentCategory {
  private static final String USER_PROFILE_ID = "INSERT_USER_PROFILE_ID_HERE";

  private static final String CONTENT_CATEGORY_NAME = "INSERT_CONTENT_CATEGORY_NAME_HERE";

  public static void runExample(Dfareporting reporting, long profileId, String contentCategoryName)
      throws Exception {
    // Create the content category.
    ContentCategory contentCategory = new ContentCategory();
    contentCategory.setName(contentCategoryName);

    // Insert the content category.
    ContentCategory result =
        reporting.contentCategories().insert(profileId, contentCategory).execute();

    // Display the new content category ID.
    System.out.printf("Content category with ID %d was created.%n", result.getId());
  }

  public static void main(String[] args) throws Exception {
    Dfareporting reporting = DfaReportingFactory.getInstance();

    long profileId = Long.parseLong(USER_PROFILE_ID);

    runExample(reporting, profileId, CONTENT_CATEGORY_NAME);
  }
}
