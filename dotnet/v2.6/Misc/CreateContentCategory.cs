/*
 * Copyright 2015 Google Inc
 *
 * Licensed under the Apache License, Version 2.0(the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

using System;
using Google.Apis.Dfareporting.v2_6;
using Google.Apis.Dfareporting.v2_6.Data;

namespace DfaReporting.Samples {
  /// <summary>
  /// This example creates a content category with the given name and
  /// description.
  /// </summary>
  class CreateContentCategory : SampleBase {
    /// <summary>
    /// Returns a description about the code example.
    /// </summary>
    public override string Description {
      get {
        return "This example creates a content category with the given name" +
            " and description.\n";
      }
    }

    /// <summary>
    /// Main method, to run this code example as a standalone application.
    /// </summary>
    /// <param name="args">The command line arguments.</param>
    public static void Main(string[] args) {
      SampleBase codeExample = new CreateContentCategory();
      Console.WriteLine(codeExample.Description);
      codeExample.Run(DfaReportingFactory.getInstance());
    }

    /// <summary>
    /// Run the code example.
    /// </summary>
    /// <param name="service">An initialized Dfa Reporting service object
    /// </param>
    public override void Run(DfareportingService service) {
      long profileId = long.Parse(_T("INSERT_USER_PROFILE_ID_HERE"));

      String contentCategoryName = _T("INSERT_CONTENT_CATEGORY_NAME_HERE");

      // Create the content category.
      ContentCategory contentCategory = new ContentCategory();
      contentCategory.Name = contentCategoryName;

      // Insert the content category.
      ContentCategory result =
          service.ContentCategories.Insert(contentCategory, profileId).Execute();

      // Display the new content category ID.
      Console.WriteLine("Content category with ID {0} was created.", result.Id);
    }
  }
}
