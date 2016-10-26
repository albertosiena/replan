class Release < ApplicationRecord
  belongs_to :project
  has_many :features, dependent: :nullify
  has_and_belongs_to_many :resources
  has_one :plan
  validates :project_id, presence: true
  validates :starts_at, presence: true
  validates :deadline, presence: true
  validate :start_must_be_before_deadline

  private

  def start_must_be_before_deadline
    errors.add(:starts_at, "must be before deadline") unless
        starts_at < deadline
  end 
end
