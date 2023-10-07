Feature: Points / MindMap service

  Scenario: Add a new person to point service
    Given the user details below:
      | _service         | person         |
      | person_age       | 90             |
      | person_name      | Abe            |
      | person_cellphone | +123487866     |
      | person_email     | work@gmail.com |
      | person_gender    | female         |
    When user is added to point service database
    Then check that the user is stored

  Scenario: post a point to the point service using any person
    Given any person exists in the db:
      | _service        | person                                             |
      | person_minAge   | 20                                                 |
      | person_maxAge   | 20                                                  |
      | person_gender   | female                                             |
      | asking_scenario | post a point to the point service using any person |
    When this person posts a point with below details:
      | _contextCall    | context_person_personId                  |
      | _service        | points                                   |
      | point_pointCall | hopeful_about_wealth                     |
      | point_personId  | context_person_personId                  |
      | point_mood      | SELF                                     |
      | point_category  | WELL BEING                               |
      | point_subject   | Syncing                                  |
      | point_note      | A point in time that you wish to record. |
    Then verify that the point is retrievable via the "http://localhost:8080/point" service end_point

  Scenario: Get an image from point service using the filename request param.
    Given a user uploads image of type "image/jpeg":
      | _service     | media           |
      | Point-Call   | I_do_wonder_if. |
      | organization | just_points     |
      | type         | image           |
      | filename     | nature.jpg      |
    Then the user can get image "nature.jpg" back via "http://localhost:8080/media"

  Scenario: Get all points
    Given get all points service is enquired:
      | _service | points |
