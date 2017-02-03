class Plan < ApplicationRecord
  belongs_to :release
  has_many :jobs, :dependent => :destroy
  
  def self.get_plan(release)
    if release.plan.nil? || release.plan.deprecated?
      if release.starts_at.nil?
        FakePlanner.plan(release)
      else
        ValentinPlanner.plan(release)
      end
    end
    return release.plan
  end
  
  def deprecate
    self.updated_at = DateTime.now
    self.save
  end
  
  def deprecated?
    self.created_at != self.updated_at
  end
end
