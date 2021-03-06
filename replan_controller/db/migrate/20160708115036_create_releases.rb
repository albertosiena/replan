class CreateReleases < ActiveRecord::Migration[5.0]
  def change
    create_table :releases do |t|
      t.string :name
      t.string :description
      t.references :project, foreign_key: true
      t.date :deadline

      t.timestamps
    end
  end
end
