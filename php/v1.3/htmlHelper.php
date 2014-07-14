<?php
/*
 * Copyright 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Helper functions for HTML generation.
 *
 * @author Jonathon Imperiosi <jimper@google.com>
 */

/**
 * Opens the HTML.
 * @param string $title the title of the page
 */
function printHtmlHeader($title) {
  $htmlTitle = filter_var($title, FILTER_SANITIZE_SPECIAL_CHARS);
  print '<!DOCTYPE html>';
  print '<html>';
  printf('<head><title>%s</title></head>', $htmlTitle);
  print '<link rel="stylesheet" href="styles/style.css" />';
  print '<body>';
}

/**
 * Closes the HTML.
 */
function printHtmlFooter() {
  print '</body>';
  print '</html>';
}

/**
 * Closes the HTML for samples.
 */
function printSampleHtmlFooter() {
  print '<br><a href="index.php">Go back to samples list</a>';
  printHtmlFooter();
}

/**
 * Prints the index with links to the examples.
 * @param array $actions supported actions
 */
function printExamplesIndex($actions) {
  print '<h2>Select a sample from the list</h2>';
  print '<ul class="nav">';

  foreach ($actions as $action => $sub_actions) {
    printf('<li><b>%s</b><ul>', $action);

    foreach ($sub_actions as $sub_action) {
      include_once 'examples/' . $sub_action . '.php';
      $class = ucfirst($sub_action);
      printf('<li><a href="?action=%s">%s</a></li>', $sub_action,
        $class::getName());
    }

    print '<li>&nbsp;</li></ul></li>';
  }

  print '</ul>';
}
